package on.ssgdeal.promotion_service.domain.entity.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record GetPromotionsDto(
        Long promotionId,
        String promotionTitle,
        String promotionPreviewUrl,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate
) {
}