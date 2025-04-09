package on.ssgdeal.order_service.application.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.CreateUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.application.service.dto.TotalOrderProductInfo;
import on.ssgdeal.order_service.application.service.dto.TotalOrderProductInfo.ProductInfo;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.OrderProduct;
import on.ssgdeal.order_service.domain.entity.Orderer;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.TotalOrderPayment;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderProductDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateTotalOrderDto;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.exception.OrderException;
import on.ssgdeal.order_service.exception.OrderExceptionCode;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoRequestDto.GetProductDetails;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationResponseDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "OrderService")
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final PromotionService promotionService;
    private final TotalOrderRepository totalOrderRepository;
    private final UserService userService;

    @Override
    @Transactional
    public CreateOrderResponse createOrder(CreateOrderRequestDto request,
        LoginUserInfoDto loginUserInfoDto) {

        log.info("주문 생성 요청: {}", request);

        // 배송지 검증
        ValidDestinationResponseDto validDestinationResponseDto = validDestinationRequestDto(
            request.destinationId());

        // 상품 정보 조회 요청
        var productInfo = getGetProductInfoAndStockDecreaseResponseDto(request);

        // 상품 재고 감소 요청
        productStockRequest(productInfo);

        List<CreateOrderDto> orderDtos = getCreateOrderDtos(productInfo);
        String totalOrderNumber = getTotalOrderNumber();
        long totalOrderPrice = getTotalOrderPrice(orderDtos);

        // totalOrder 엔티티 생성
        CreateTotalOrderDto creteTotalOrderDto = CreateTotalOrderDto.from(totalOrderNumber,
            totalOrderPrice);
        TotalOrder totalOrder = TotalOrder.create(creteTotalOrderDto);

        // Orderer 생성
        CreateUserInfoDto createUserInfoDto = CreateUserInfoDto.from(request, loginUserInfoDto,
            validDestinationResponseDto);
        Orderer orderer = Orderer.create(totalOrder, createUserInfoDto);

        // totalOrderPayment 엔티티 생성
        TotalOrderPayment totalOrderPayment = TotalOrderPayment.create(totalOrder);

        // OrderProduct, Order 엔티티 생성
        List<Order> orders = savedOrdersAndOrderProducts(totalOrder, orderDtos);

        // totalOrder 저장
        totalOrder.addDependencies(orderer, totalOrderPayment, orders);
        totalOrderRepository.save(totalOrder);

        return CreateOrderResponse.from(totalOrder);
    }

    protected ValidDestinationResponseDto validDestinationRequestDto(Long destinationId) {
        ValidDestinationRequestDto validDestinationRequest = ValidDestinationRequestDto.from(
            destinationId);
        try {
            log.info("배송지 검증 요청: {}", validDestinationRequest.destinationId());
            var validDestinationResponseDto = userService.validDestinationRequest(
                validDestinationRequest);
            log.info("배송지 검증 완료");
            return validDestinationResponseDto;
        } catch (Exception e) {
            log.info("배송지 검증 실패: {}", e.getMessage());
            throw new OrderException(OrderExceptionCode.ORDER_VALID_DESTINATION);
        }
    }

    protected void productStockRequest(GetProductInfoDto productInfo) {
        List<DecreaseProductStockRequestDto> decreaseRequest = toDecreaseRequest(productInfo);
        List<DecreaseProductStockResponseDto> decreaseSuccessList = new ArrayList<>();
        try {
            for (DecreaseProductStockRequestDto decreaseRequestDto : decreaseRequest) {
                log.info("재고 감소 요청 productId: {}", decreaseRequestDto.productId());
                DecreaseProductStockResponseDto decreaseProductStockResponseDto = promotionService.decreaseProductStock(
                    decreaseRequestDto);
                decreaseSuccessList.add(decreaseProductStockResponseDto);
                log.info("재고 감소 요청 성공 productId: {}", decreaseProductStockResponseDto.productId());
            }
        } catch (Exception e) {
            log.info("재고 감소 요청 실패, 롤백 요청 : {}", e.getMessage());
            List<InCreaseProductStockRequestDto> increaseRequest = toIncreaseRequest(
                decreaseSuccessList);
            for (InCreaseProductStockRequestDto increaseRequestDto : increaseRequest) {
                log.info("롤백 요청 productId: {}", increaseRequestDto.productId());
                promotionService.increaseProductStock(increaseRequestDto);
            }
        }
    }

    private List<Order> savedOrdersAndOrderProducts(TotalOrder totalOrder,
        List<CreateOrderDto> orderDtos) {
        List<Order> orders = Order.create(totalOrder, orderDtos);
        for (int i = 0; i < orders.size(); i++) {
            Order order = orders.get(i);
            CreateOrderDto createOrderDto = orderDtos.get(i);

            List<OrderProduct> orderProducts = createOrderDto.products().stream()
                .map(productDto -> OrderProduct.create(order, productDto)).toList();

            order.addOrderProductDependency(orderProducts);
        }
        return orders;
    }

    private List<CreateOrderDto> getCreateOrderDtos(GetProductInfoDto productInfoAndStockDecrease) {
        List<CreateOrderDto> orderDtos = productInfoAndStockDecrease.companyList().stream()
            .map(companyInfo -> {
                List<CreateOrderProductDto> productDtos = companyInfo.companyProductList().stream()
                    .map(product -> {
                        Long totalPrice = (product.promotionPrice() + product.extraPrice())
                            * product.decreaseStockAmount();
                        return CreateOrderProductDto.from(product, totalPrice);
                    }).toList();

                Long orderTotalPrice = productDtos.stream()
                    .mapToLong(CreateOrderProductDto::totalPrice).sum();
                return CreateOrderDto.from(orderTotalPrice, companyInfo.companyId(),
                    companyInfo.companyName(), productDtos);
            }).toList();
        return orderDtos;
    }

    private long getTotalOrderPrice(List<CreateOrderDto> orderDtos) {
        return orderDtos.stream().mapToLong(CreateOrderDto::orderTotalPrice).sum();
    }

    //todo: 주문번호 저장 로직 변경 -> 현재 랜덤 번호 부여, 시퀀스로 변환 필요.
    private String getTotalOrderNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = String.format("%04d", new Random().nextInt(10000));
        return datePart + randomPart;
    }

    private GetProductInfoDto getGetProductInfoAndStockDecreaseResponseDto(
        CreateOrderRequestDto request) {
        TotalOrderProductInfo totalOrderProductInfo = convertPromotionRequestProductInfo(request);
        var promotionRequestInfo = fromTotalOrderProductInfo(totalOrderProductInfo);

        GetProductInfoDto productInfoDto;
        try {
            log.info("Order > Promotion");
            log.info("주문 생성 상품 정보 조회 요청 : {}", promotionRequestInfo);
            productInfoDto = promotionService.getProductInfoAndStockDecrease(
                promotionRequestInfo);
            log.info("상품 정보 조회 성공 : {}", productInfoDto);
        } catch (Exception e) {
            log.info("조회 실패 및 재고 감소 불가능 상황 : {}", e.getMessage());
            throw new OrderException(OrderExceptionCode.ORDER_PROMOTION_STOCK_OVER);
        }
        return productInfoDto;
    }

    private TotalOrderProductInfo convertPromotionRequestProductInfo(
        CreateOrderRequestDto request) {
        return new TotalOrderProductInfo(request.subOrders().stream().flatMap(
            sub -> sub.orderedProducts().stream()
                .map(p -> new ProductInfo(p.productId(), p.optionId(), p.quantity()))).toList());
    }

    protected GetProductInfoRequestDto fromTotalOrderProductInfo(
        TotalOrderProductInfo totalOrderProductInfo) {
        List<GetProductDetails> details = totalOrderProductInfo.products().stream().map(
            p -> new GetProductInfoRequestDto.GetProductDetails(p.productId(), p.optionId(),
                p.quantity())).toList();

        return new GetProductInfoRequestDto(details);
    }

    protected List<DecreaseProductStockRequestDto> toDecreaseRequest(
        GetProductInfoDto getProductInfoDto) {
        return getProductInfoDto.companyList().stream()
            .flatMap(company -> company.companyProductList().stream()).map(
                product -> DecreaseProductStockRequestDto.from(product.productId(),
                    product.optionId(), product.decreaseStockAmount())).toList();

    }

    protected List<InCreaseProductStockRequestDto> toIncreaseRequest(
        List<DecreaseProductStockResponseDto> decreaseSuccessList) {
        return decreaseSuccessList.stream().map(
            product -> InCreaseProductStockRequestDto.from(product.productId(), product.optionId(),
                product.decreaseStockAmount())).toList();
    }
}
