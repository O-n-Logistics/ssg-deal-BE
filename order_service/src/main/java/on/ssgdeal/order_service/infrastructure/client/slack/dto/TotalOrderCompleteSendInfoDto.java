package on.ssgdeal.order_service.infrastructure.client.slack.dto;

import java.time.LocalDate;

public record TotalOrderCompleteSendInfoDto(LocalDate orderAt, Long paymentPrice) {

    public static TotalOrderCompleteSendInfoDto from(LocalDate orderAt, Long paymentPrice) {
        return new TotalOrderCompleteSendInfoDto(orderAt, paymentPrice);
    }

}
