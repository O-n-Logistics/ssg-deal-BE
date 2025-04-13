package on.ssgdeal.promotion_service.application.service;

import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.application.service.dto.GetInProgressPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.application.service.dto.GetPromotionsRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.GetPromotionsResponseDto;
import org.springframework.data.domain.Pageable;

public interface PromotionService {
    GetFinishedPromotionDetailResponseDto getFinishedPromotionDetail(Long promotionId);
    GetInProgressPromotionDetailResponseDto getInProgressPromotionDetail(Long promotionId, Pageable pageable);
    PageDto<GetPromotionsResponseDto> getPromotions(GetPromotionsRequestDto requestDto);
}
