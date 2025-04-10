package on.ssgdeal.order_service.infrastructure.client.slack.feign.dto;

import java.time.LocalDate;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.entity.dtos.UpdateTotalOrderSuccessDto;
import on.ssgdeal.order_service.domain.enums.TotalOrderStatus;
import on.ssgdeal.order_service.infrastructure.client.slack.dto.TotalOrderCompleteSendInfoDto;

public record OrderCompleteSendSlackRequestDto(Long totalOrderId, String slackEmail,
                                               LocalDate OrderAt, Long paymentPrice,
                                               TotalOrderStatus orderStatus) {

    public static OrderCompleteSendSlackRequestDto from(
        UpdateTotalOrderSuccessDto updateTotalOrderSuccessDto,
        TotalOrderCompleteSendInfoDto totalOrderCompleteSendInfoDto,
        LoginUserInfoDto loginUserInfoDto) {
        return new OrderCompleteSendSlackRequestDto(updateTotalOrderSuccessDto.totalOrderId(),
            loginUserInfoDto.slackEmail(), totalOrderCompleteSendInfoDto.orderAt(),
            totalOrderCompleteSendInfoDto.paymentPrice(), TotalOrderStatus.EXPIRED);
    }
}
