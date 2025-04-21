package on.ssgdeal.order_service.application.command.support;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.PromotionService;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dtos.InCreaseProductStockRequestDto;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StockManager {

    private final PromotionService promotionService;

    /**
     * 주어진 상품 정보에 따라 각 상품의 재고를 감소시키며, 실패 시 성공적으로 감소된 재고를 롤백합니다.
     *
     * @param productInfo 재고 감소를 요청할 상품 정보 DTO
     */
    public void decreaseStock(GetProductInfoDto productInfo) {
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

    /**
     * GetProductInfoDto에서 각 상품의 재고 감소 요청 DTO 목록을 생성합니다.
     *
     * @param getProductInfoDto 재고 감소가 필요한 상품 정보가 포함된 DTO
     * @return 각 상품별로 생성된 DecreaseProductStockRequestDto 리스트
     */
    protected List<DecreaseProductStockRequestDto> toDecreaseRequest(
        GetProductInfoDto getProductInfoDto) {
        return getProductInfoDto.companyList().stream()
            .flatMap(company -> company.companyProductList().stream()).map(
                product -> DecreaseProductStockRequestDto.from(product.productId(),
                    product.optionId(), product.decreaseStockAmount())).toList();

    }

    /**
     * 성공적으로 감소된 재고 응답 목록을 재고 복구 요청 DTO 목록으로 변환합니다.
     *
     * @param decreaseSuccessList 재고 감소에 성공한 응답 DTO 리스트
     * @return 재고 복구를 위한 요청 DTO 리스트
     */
    protected List<InCreaseProductStockRequestDto> toIncreaseRequest(
        List<DecreaseProductStockResponseDto> decreaseSuccessList) {
        return decreaseSuccessList.stream().map(
            product -> InCreaseProductStockRequestDto.from(product.productId(), product.optionId(),
                product.decreaseStockAmount())).toList();
    }
}
