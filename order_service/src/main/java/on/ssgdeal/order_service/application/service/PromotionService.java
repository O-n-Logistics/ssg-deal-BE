package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.DecreaseProductStockResponseDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.GetProductInfoRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.InCreaseProductStockRequestDto;
import on.ssgdeal.order_service.infrastructure.client.promotion.feign.dto.InCreaseProductStockResponseDto;

public interface PromotionService {

    GetProductInfoDto getProductInfoAndStockDecrease(
        GetProductInfoRequestDto dto);

    InCreaseProductStockResponseDto increaseProductStock(
        InCreaseProductStockRequestDto dto
    );

    DecreaseProductStockResponseDto decreaseProductStock(
        DecreaseProductStockRequestDto dto
    );
}
