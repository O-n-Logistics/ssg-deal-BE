package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos.CancelTotalOrderPaymentResponseDto;

public interface PaymentService {

    CancelTotalOrderPaymentResponseDto cancelTotalOrderPayment(Long totalOrderId,
        CancelTotalOrderPaymentRequestDto requestDto);

}