package on.ssgdeal.order_service.infrastructure.persistence.jpa.querydsl;

import static on.ssgdeal.order_service.domain.entity.QOrder.order;
import static on.ssgdeal.order_service.domain.entity.QTotalOrder.totalOrder;
import static on.ssgdeal.order_service.domain.entity.QTotalOrderPayment.totalOrderPayment;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.domain.entity.TotalOrder;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.OrderStatus;
import on.ssgdeal.order_service.domain.enums.PaymentStatus;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j(topic = "TotalOrderQueryRepositoryImpl")
public class TotalOrderQueryRepositoryImpl implements TotalOrderQueryRepository {

    private final JPAQueryFactory queryFactory;


    @Override
    public void paymentSuccess(TotalOrder requestTotalOrder,
        UpdateTotalOrderSuccessDto requestDto) {
        queryFactory.update(totalOrder)
            .set(totalOrder.status, TotalOrderStatus.EXPIRED)
            .where(totalOrder.id.eq(requestTotalOrder.getId()))
            .execute();
        queryFactory.update(order)
            .set(order.status, OrderStatus.PAID)
            .where(order.totalOrder.eq(requestTotalOrder))
            .execute();
        queryFactory.update(totalOrderPayment)
            .set(totalOrderPayment.paymentId, requestDto.paymentId())
            .set(totalOrderPayment.paymentType, requestDto.paymentType())
            .set(totalOrderPayment.paymentMethod, requestDto.paymentMethod())
            .set(totalOrderPayment.paymentDate, requestDto.paymentDate())
            .set(totalOrderPayment.paymentAmount, requestDto.paymentAmount())
            .set(totalOrderPayment.paymentKey, requestDto.paymentKey())
            .set(totalOrderPayment.paymentStatus, PaymentStatus.COMPLETED)
            .where(totalOrderPayment.totalOrder.eq(requestTotalOrder))
            .execute();
    }

}
