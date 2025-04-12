package on.ssgdeal.promotion_service.domain.repository;

import on.ssgdeal.promotion_service.domain.entity.Promotion;

import java.util.Optional;

public interface PromotionRepository {

    Optional<Promotion> findById(Long id);

}
