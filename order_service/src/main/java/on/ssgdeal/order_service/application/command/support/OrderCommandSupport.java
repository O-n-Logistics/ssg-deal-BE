package on.ssgdeal.order_service.application.command.support;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dtos.ValidDestinationResponseDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderCommandSupport {

    private final DestinationValidator destinationValidator;
    private final ProductValidator productValidator;
    private final StockManager stockManager;

    /**
     * 지정된 목적지 ID의 유효성을 검사하고 결과를 반환합니다.
     *
     * @param destinationId 검사할 목적지의 ID
     * @return 목적지 유효성 검사 결과 정보
     */
    public ValidDestinationResponseDto validDestination(Long destinationId) {
        return destinationValidator.valid(destinationId);
    }

    /**
     * 주문 요청 정보를 기반으로 상품 정보를 검증하고 반환합니다.
     *
     * @param request 주문 생성 요청 데이터
     * @return 검증된 상품 정보 DTO
     */
    public GetProductInfoDto validProductInfo(CreateOrderRequestDto request) {
        return productValidator.valid(request);
    }

    /**
     * 주어진 상품 정보에 따라 재고를 차감합니다.
     *
     * @param productInfo 재고 차감에 사용할 상품 정보
     */
    public void decreaseStock(GetProductInfoDto productInfo) {
        stockManager.decreaseStock(productInfo);
    }

}
