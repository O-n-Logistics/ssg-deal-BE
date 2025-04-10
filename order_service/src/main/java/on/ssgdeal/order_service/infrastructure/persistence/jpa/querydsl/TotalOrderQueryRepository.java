package on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl;

import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;

public interface TotalOrderQueryRepository {

    void paymentSuccess(TotalOrder totalOrder,
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto);
}
