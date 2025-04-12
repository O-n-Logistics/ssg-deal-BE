package on.ssgdeal.promotion_service.application.service;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.enums.PromotionStatus;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.exception.PromotionException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;

    @Override
    public GetFinishedPromotionDetailResponseDto getFinishedPromotionDetail(Long promotionId) {
        Promotion promotion = promotionRepository.findById(promotionId)
                .orElseThrow(PromotionException.PromotionNotFoundException::new);
        validatePromotionIsFinished(promotion);
        return GetFinishedPromotionDetailResponseDto.from(promotion);
    }

    private static void validatePromotionIsFinished(Promotion promotion) {
        if (promotion.getEndPromotionDate().isAfter(LocalDate.now())
                || promotion.getStatus() != PromotionStatus.FINISHED
        ) {
            throw new PromotionException.PromotionNotFinishedException();
        }
    }
}
