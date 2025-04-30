package on.ssgdeal.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationFailureHandler {

    private final NotificationRepository notificationRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Notification saveFailureRecord(
            CreateNotificationRequestDto requestDto,
            String content,
            NotificationTemplate template
    ) {
        log.info("슬랙 실패 알림 생성");
        Notification notification = Notification.createFailure(
                CreateNotificationRequestDto.toDto(
                        requestDto,
                        content,
                        template,
                        LocalDateTime.now()
                )
        );
        return notificationRepository.save(notification);
    }
}
