package on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos;

public record CancelTotalOrderPaymentRequestDto(
    String cancelReason,
    Long cancelAmount
) {

    public static CancelTotalOrderPaymentRequestDto from(
        String cancelReason,
        Long cancelAmount
    ) {
        return new CancelTotalOrderPaymentRequestDto(cancelReason, cancelAmount);
    }

}
