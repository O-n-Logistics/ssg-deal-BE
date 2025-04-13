package on.ssgdeal.promotion_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.application.service.dto.GetPromotionsResponseDto;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.entity.dto.GetInProgressPromotionDetailDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsConditionDto;
import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsDto;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.PromotionJpaRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.querydsl.PromotionQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {

    private final PromotionJpaRepository promotionJpaRepository;
    private final PromotionQueryDslRepository promotionQueryDslRepository;

    @Override
    public Optional<Promotion> findById(Long id) {
        return promotionJpaRepository.findById(id);
    }

    @Override
    public Optional<GetInProgressPromotionDetailDto> findPromotionWithProductsById(Long promotionId, Pageable pageable) {
        return promotionQueryDslRepository.findPromotionWithProductsById(promotionId, pageable);
    }
    @Override
    public Page<GetPromotionsDto> findPromotions(GetPromotionsConditionDto conditionDto) {
        return promotionQueryDslRepository.findPromotions(conditionDto);
    }
}
