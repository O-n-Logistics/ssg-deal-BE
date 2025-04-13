package on.ssgdeal.promotion_service.domain.repository;

import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository {

    Optional<Promotion> findById(Long id);
    Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long id, Pageable pageable);
    Page<Promotion> findPromotions(GetPromotionsConditionDto conditionDto);

}
