package on.ssgdeal.payment_service.infrastructure.client.TossPaymentClient.dto.request;

public record PaymentPartialCancelRequestDto(
    String cancelReason,
    Long cancelAmount
) {

}
