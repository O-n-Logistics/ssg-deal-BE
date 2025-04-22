package on.ssgdeal.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import on.ssgdeal.notification_service.domain.entity.Notification;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationChannelType;
import on.ssgdeal.notification_service.domain.repository.NotificationRepository;
import on.ssgdeal.notification_service.exception.NotificationException;
import on.ssgdeal.notification_service.infrastructure.client.slack.converter.SlackTimestampToKSTConverter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class SlackNotificationSender implements NotificationSender {

    private final SlackClient slackClient;
    private final SlackTimestampToKSTConverter timestampConverter;
    private final NotificationRepository notificationRepository;
    private final NotificationFailureHandler failureHandler;

    @Override
    public NotificationChannelType getChannelType() {
        return NotificationChannelType.SLACK;
    }

    @Override
    public CreateNotificationResponseDto sendNotification(
            CreateNotificationRequestDto requestDto,
            String content,
            NotificationTemplate template
    ) {
        try {
            String timestamp = slackClient.sendNotificationToUser(requestDto.receiverSlackEmail(), content);
            LocalDateTime sendAt = timestampConverter.convertToKST(timestamp);

            log.info("슬랙 성공 알림 생성");
            Notification notification = Notification.createSuccess(
                    CreateNotificationRequestDto.toDto(
                            requestDto,
                            content,
                            template,
                            sendAt
                    )
            );
            notificationRepository.save(notification);
            return CreateNotificationResponseDto.from(notification);

        } catch (Exception e) {

            Notification failedNotification = failureHandler.saveFailureRecord(requestDto, content, template);

            log.error("슬랙 전송 실패 알림 ID: {}", failedNotification.getId());
            throw new NotificationException.SlackApiErrorException();
        }
    }
}
