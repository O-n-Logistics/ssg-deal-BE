package on.ssgdeal.order_service.infrastructure.client.payments;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.order_service.application.service.PaymentService;
import on.ssgdeal.order_service.infrastructure.client.payments.feign.PaymentServiceFeignClient;
import on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos.CancelTotalOrderPaymentResponseDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "PaymentServiceImpl")
public class PaymentServiceImpl implements PaymentService {

    private final PaymentServiceFeignClient feignClient;

    @Override
    public CancelTotalOrderPaymentResponseDto cancelTotalOrderPayment(Long totalOrderId,
        CancelTotalOrderPaymentRequestDto requestDto) {
        log.info("Payment request: {}", totalOrderId);
        return feignClient.cancelTotalOrderPayment(totalOrderId, requestDto).data();
    }
}