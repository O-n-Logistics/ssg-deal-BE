package on.ssgdeal.notification_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.notification_service.application.service.NotificationService;
import on.ssgdeal.notification_service.presentation.internal.dto.CreateNotificationRequest;
import on.ssgdeal.notification_service.presentation.internal.dto.CreateNotificationResponse;
import on.ssgdeal.notification_service.presentation.internal.mapper.NotificationRequestMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j(topic = "NotificationController")
@RestController
@RequestMapping("/api/v1/slack")
@RequiredArgsConstructor
public class NotificationController {

    private final PassportUtil passportUtil;
    private final NotificationRequestMapper notificationMapper;
    private final NotificationService notificationService;

    @PostMapping("/order/complete")
    public ResponseEntity<CommonResponse<CreateNotificationResponse>> createNotification(
            @Valid @RequestBody final CreateNotificationRequest request,
            HttpServletRequest servletRequest
    ) {
        Passport passport = passportUtil.getPassportBy(servletRequest);
        final var requestDto = notificationMapper.toDto(request, passport.getSlackEmail());
        log.info("주문 완료 슬랙 메시지 요청 : {}", request);
        final var responseDto = notificationService.sendSlackNotification(requestDto);
        return ResponseEntity.ok().body(CommonResponse.success(responseDto));
    }
}
