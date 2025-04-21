package on.ssgdeal.order_service.application.command;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.command.Command;
import on.ssgdeal.order_service.application.command.support.OrderCommandSupport;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.CreateUserInfoDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.entity.Order;
import on.ssgdeal.order_service.domain.entity.OrderProduct;
import on.ssgdeal.order_service.domain.entity.Orderer;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.TotalOrderPayment;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateOrderProductDto;
import on.ssgdeal.order_service.domain.entity.dtos.CreateTotalOrderDto;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import on.ssgdeal.order_service.util.OrderNumberGenerator;

@Slf4j
@RequiredArgsConstructor
public class CreateOrderCommand implements Command<TotalOrder> {

    private final CreateOrderRequestDto request;
    private final LoginUserInfoDto loginUserInfoDto;
    private final TotalOrderRepository totalOrderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderCommandSupport support;

    /**
     * 주문 요청을 검증하고, 주문 관련 엔티티를 생성 및 저장하여 최종 주문을 생성합니다.
     *
     * @return 생성된 TotalOrder 엔티티
     */
    @Override
    public TotalOrder execute() {

        ValidDestinationResponseDto validDestinationResponseDto = support.validDestination(
            request.destinationId());

        GetProductInfoDto productInfo = support.validProductInfo(request);
        support.decreaseStock(productInfo);

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
        TotalOrder savedTotalOrder = totalOrderRepository.save(totalOrder);

        log.info("주문 생성 완료 : {}", savedTotalOrder.getId());
        return savedTotalOrder;
    }

    /**
     * 주문 생성 작업을 취소하는 기능은 현재 구현되어 있지 않습니다.
     */
    @Override
    public void undo() {

    }

    /**
     * 주문 DTO 목록을 기반으로 주문 및 해당 주문 상품 엔티티를 생성하여 반환합니다.
     *
     * @param totalOrder 생성할 주문이 속한 전체 주문 엔티티
     * @param orderDtos 각 회사별 주문 정보를 담은 DTO 목록
     * @return 생성된 주문(Order) 엔티티 목록
     */
    private List<Order> savedOrdersAndOrderProducts(
        TotalOrder totalOrder,
        List<CreateOrderDto> orderDtos
    ) {
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

    /**
     * 주문 DTO 목록의 총 주문 금액을 계산합니다.
     *
     * @param orderDtos 각 주문의 정보를 담은 CreateOrderDto 리스트
     * @return 모든 주문의 총 금액
     */
    private long getTotalOrderPrice(List<CreateOrderDto> orderDtos) {
        return orderDtos.stream().mapToLong(CreateOrderDto::orderTotalPrice).sum();
    }

    /**
     * 상품 정보와 재고 감소 정보를 기반으로 회사별 주문 DTO 목록을 생성합니다.
     *
     * @param productInfoAndStockDecrease 각 회사의 상품 정보와 재고 감소 정보를 포함한 DTO
     * @return 회사별 주문 정보를 담은 CreateOrderDto 리스트
     */
    private List<CreateOrderDto> getCreateOrderDtos(GetProductInfoDto productInfoAndStockDecrease) {
        return productInfoAndStockDecrease.companyList().stream()
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
    }

    /**
     * 주문에 사용할 고유한 주문 번호를 생성하여 반환합니다.
     *
     * @return 생성된 주문 번호
     */
    private String getTotalOrderNumber() {
        return orderNumberGenerator.generateOrderNumber();
    }
}
