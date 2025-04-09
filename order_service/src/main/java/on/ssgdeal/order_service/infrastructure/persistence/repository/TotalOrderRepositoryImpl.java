package on.ssgdeal.order_service.infrastructure.persistence.repository;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl.TotalOrderJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class TotalOrderRepositoryImpl implements TotalOrderRepository {

    private final TotalOrderJpaRepository jpaRepository;

    @Override
    public TotalOrder save(TotalOrder totalOrder) {
        return jpaRepository.save(totalOrder);
    }

    @Override
    public Optional<TotalOrder> findById(Long id) {
        return jpaRepository.findById(id);
    }
}
