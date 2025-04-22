package on.ssgdeal.common.messaging.domain.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Topic {

    ORDER_SUCCESS_NOTIFICATION_EVENT("ssgdeal.orderSuccessNotificationEvent"),
    INCREASE_STOCK_EVENT("ssgdeal.increaseStockEvent"),
    ;

    private final String value;

    @Override
    public String toString() {
        return value;
    }
}
