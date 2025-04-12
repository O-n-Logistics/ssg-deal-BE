package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;

public interface PromotionService {
    GetFinishedPromotionDetailResponseDto getFinishedPromotionDetail(Long promotionId);
}
