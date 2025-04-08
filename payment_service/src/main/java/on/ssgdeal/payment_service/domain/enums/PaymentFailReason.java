package on.ssgdeal.payment_service.domain.enums;

public enum PaymentFailReason {
    INSUFFICIENT_BALANCE("잔액 부족"),
    CANCELLED_BY_USER("사용자 취소"),
    TIMEOUT("결제 처리 시간 초과"),
    PAYMENT_DECLINED("결제 거절"),
    ORDER_NOT_FOUND("주문 정보 없음"),
    INVALID_AMOUNT("잘못된 결제 금액"),
    ;

    private String description;

    PaymentFailReason(String description) {
        this.description = description;
    }
}
