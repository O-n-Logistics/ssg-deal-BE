package on.ssgdeal.promotion_service.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.promotion_service.domain.entity.Promotion;
import on.ssgdeal.promotion_service.domain.repository.PromotionRepository;
import on.ssgdeal.promotion_service.infrastructure.persistence.jpa.JpaPromotionRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PromotionRepositoryImpl implements PromotionRepository {

    private final JpaPromotionRepository jpaPromotionRepository;

    @Override
    public Optional<Promotion> findById(Long id) {
        return jpaPromotionRepository.findById(id);
    }
}
