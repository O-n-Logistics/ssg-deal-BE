package on.ssgdeal.payment_service.presentation.internal;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.payment_service.application.service.PaymentProcessorService;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentCancelResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/internal/v1/payments")
public class PaymentInternalController {

    private final PaymentProcessorService paymentProcessorService;

    @PostMapping("/{totalOrderId}/cancel")
    public ResponseEntity<CommonResponse<OrderPaymentCancelResponseDto>> orderCancelPayment(
        @PathVariable Long totalOrderId) {
        final var responseDto = paymentProcessorService.orderPaymentCancel(totalOrderId);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }
}
