package on.ssgdeal.order_service.domain.repository;

import java.util.Optional;
import on.ssgdeal.order_service.domain.entity.TotalOrder;

public interface TotalOrderRepository {

    TotalOrder save(TotalOrder totalOrder);

    Optional<TotalOrder> findById(Long id);
}
