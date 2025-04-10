package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.slack.feign.dto.OrderCompleteSendSlackRequestDto;
import on.ssgdeal.order_service.infrastructure.client.slack.feign.dto.OrderCompleteSendSlackResponseDto;

public interface SlackService {

    OrderCompleteSendSlackResponseDto sendOrderCompleteMessage(
        OrderCompleteSendSlackRequestDto requestDto);
}
