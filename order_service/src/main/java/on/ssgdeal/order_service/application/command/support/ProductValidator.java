package on.ssgdeal.order_service.application.command.support;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.TotalOrderProductInfo;
import on.ssgdeal.order_service.application.dto.TotalOrderProductInfo.ProductInfo;
import on.ssgdeal.order_service.application.service.PromotionService;
import on.ssgdeal.order_service.exception.OrderException.OrderPromotionStockOver;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoRequestDto.GetProductDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductValidator {

    private final PromotionService promotionService;

    /**
     * 주문 생성 요청에 포함된 상품 정보와 재고를 검증하고, 프로모션 서비스에서 상품 정보를 조회한 후 재고를 차감합니다.
     *
     * @param request 주문 생성 요청 데이터
     * @return 프로모션 서비스에서 조회된 상품 정보 DTO
     * @throws OrderPromotionStockOver 프로모션 서비스에서 재고 부족 등으로 상품 정보 조회 또는 재고 차감에 실패한 경우 발생
     */
    public GetProductInfoDto valid(CreateOrderRequestDto request) {
        TotalOrderProductInfo totalOrderProductInfo = convertPromotionRequestProductInfo(request);
        var promotionRequestInfo = fromTotalOrderProductInfo(totalOrderProductInfo);

        GetProductInfoDto productInfoDto;
        try {
            log.info("Order > Promotion");
            log.info("주문 생성 상품 정보 조회 요청 : {}", promotionRequestInfo);
            productInfoDto = promotionService.getProductInfoAndStockDecrease(promotionRequestInfo);
            log.info("상품 정보 조회 성공 : {}", productInfoDto);
        } catch (Exception e) {
            log.info("조회 실패 및 재고 감소 불가능 상황 : {}", e.getMessage());
            throw new OrderPromotionStockOver();
        }
        return productInfoDto;
    }

    /**
     * 주문 요청에서 모든 하위 주문의 상품 정보를 추출하여 총 주문 상품 정보로 변환합니다.
     *
     * @param request 주문 생성 요청 DTO
     * @return 모든 주문 상품의 정보를 포함하는 TotalOrderProductInfo 객체
     */
    private TotalOrderProductInfo convertPromotionRequestProductInfo(
        CreateOrderRequestDto request
    ) {
        return new TotalOrderProductInfo(request.subOrders().stream().flatMap(
            sub -> sub.orderedProducts().stream()
                .map(p -> new ProductInfo(p.productId(), p.optionId(), p.quantity()))).toList());
    }

    /**
     * TotalOrderProductInfo 객체를 프로모션 서비스 요청에 필요한 GetProductInfoRequestDto로 변환합니다.
     *
     * @param totalOrderProductInfo 주문에 포함된 모든 상품 정보를 담은 객체
     * @return 프로모션 서비스에 전달할 상품 상세 정보 요청 DTO
     */
    private GetProductInfoRequestDto fromTotalOrderProductInfo(
        TotalOrderProductInfo totalOrderProductInfo) {
        List<GetProductDetails> details = totalOrderProductInfo.products().stream().map(
            p -> new GetProductInfoRequestDto.GetProductDetails(p.productId(), p.optionId(),
                p.quantity())).toList();

        return new GetProductInfoRequestDto(details);
    }

}
