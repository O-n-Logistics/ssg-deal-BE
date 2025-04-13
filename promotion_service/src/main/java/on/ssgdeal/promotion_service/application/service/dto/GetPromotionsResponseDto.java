package on.ssgdeal.promotion_service.application.service.dto;

import java.time.LocalDate;

public record GetPromotionsResponseDto(
        Long promotionId,
        String promotionTitle,
        String promotionPreviewUrl,
        LocalDate startPromotionDate,
        LocalDate endPromotionDate
) {
}
