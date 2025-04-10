package on.ssgdeal.payment_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.payment_service.application.service.ConfirmPaymentService;
import on.ssgdeal.payment_service.application.service.dto.response.OrderPaymentResponseDto;
import on.ssgdeal.payment_service.presentation.external.dto.OrderPaymentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payment")
public class PaymentController {

    private final ConfirmPaymentService paymentService;
    private final PassportUtil passportUtil;

    @PostMapping("/confirm")
    public ResponseEntity<CommonResponse<OrderPaymentResponseDto>> orderPayment(
        @RequestBody @Valid OrderPaymentRequest request,
        HttpServletRequest servletRequest
    ) {
        // TODO: 인증 수정하
//        final Passport passport = passportUtil.getPassportBy(servletRequest);
//        final var requestDto = request.toDto(passport.getUserId());
        final var requestDto = request.toDto(1L);
        final var responseDto = paymentService.orderPayment(requestDto);
        return ResponseEntity.ok(CommonResponse.success(responseDto));
    }
}
