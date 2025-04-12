package on.ssgdeal.payment_service.infrastructure.client.order.feign.dto;

import java.sql.Timestamp;
import lombok.Builder;
import on.ssgdeal.payment_service.domain.entity.Payment;
import on.ssgdeal.payment_service.domain.enums.PaymentMethod;
import on.ssgdeal.payment_service.domain.enums.PaymentType;

@Builder
public record CreateOrderPaymentSuccessRequestDto(
    Long totalOrderId,
    Long paymentId,
    Timestamp paymentDate,
    PaymentType paymentType,
    PaymentMethod paymentMethod,
    Long paymentAmount,
    String paymentKey
) {

    public static CreateOrderPaymentSuccessRequestDto from(Payment payment) {
        return CreateOrderPaymentSuccessRequestDto.builder()
            .totalOrderId(payment.getTotalOrderId())
            .paymentId(payment.getId())
            .paymentDate(Timestamp.valueOf(payment.getCreatedAt()))
            .paymentType(payment.getPaymentType())
            .paymentMethod(payment.getPaymentMethod())
            .paymentAmount(payment.getPaymentAmount())
            .paymentKey(payment.getPaymentKey())
            .build();
    }
}
