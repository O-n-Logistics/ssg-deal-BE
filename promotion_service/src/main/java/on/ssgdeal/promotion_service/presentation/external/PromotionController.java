package on.ssgdeal.promotion_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.promotion_service.application.service.PromotionService;
import on.ssgdeal.promotion_service.application.service.dto.GetFinishedPromotionDetailResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/finished/{promotionId}")
    public ResponseEntity<CommonResponse<GetFinishedPromotionDetailResponseDto>> getFinishedPromotionDetail(
            @PathVariable final Long promotionId
    ) {
        final var responseDto = promotionService.getFinishedPromotionDetail(promotionId);
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }

}


