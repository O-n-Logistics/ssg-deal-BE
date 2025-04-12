package on.ssgdeal.order_service.infrastructure.client.payments.feign;

import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos.CancelTotalOrderPaymentRequestDto;
import on.ssgdeal.order_service.infrastructure.client.payments.feign.dtos.CancelTotalOrderPaymentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "promotion-service")
public interface PaymentServiceFeignClient {

    @PutMapping("/internal/v1/payments/{totalOrderId}/cancel")
    CommonResponse<CancelTotalOrderPaymentResponseDto> cancelTotalOrderPayment
        (@PathVariable("totalOrderId") Long totalOrderId,
            @RequestBody CancelTotalOrderPaymentRequestDto requestDto);

}
