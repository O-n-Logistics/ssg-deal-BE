package on.ssgdeal.notification_service.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationRequestDto;
import on.ssgdeal.notification_service.application.service.dto.CreateNotificationResponseDto;
import on.ssgdeal.notification_service.domain.entity.NotificationTemplate;
import on.ssgdeal.notification_service.domain.enums.NotificationChannelType;
import on.ssgdeal.notification_service.domain.enums.NotificationTemplateType;
import on.ssgdeal.notification_service.domain.repository.NotificationTemplateRepository;
import on.ssgdeal.notification_service.exception.NotificationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Slf4j(topic = "NotificationServiceImpl")
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final Map<NotificationChannelType, NotificationSender> senderMap;
    private final NotificationTemplateRepository notificationTemplateRepository;

    @Override
    @Transactional
    public CreateNotificationResponseDto sendNotification(
            final CreateNotificationRequestDto requestDto,
            final NotificationChannelType senderType
    ) {
        log.info("알림 전송 요청 : {}", requestDto);
        NotificationTemplate template = getOrThrowNotificationTemplate(NotificationTemplateType.ORDER_COMPLETED);
        String content = createOrderCompletedSlackNotification(requestDto, template.getContent());

        NotificationSender sender = Optional.ofNullable(senderMap.get(senderType))
                .orElseThrow(NotificationException.NotificationChannelNotFoundException::new);

        return sender.sendNotification(requestDto, content, template);
    }

    private String createOrderCompletedSlackNotification(
            final CreateNotificationRequestDto requestDto,
            final String content
    ) {
        return content
                .replace("{ordererName}", requestDto.ordererName())
                .replace("{orderId}", String.valueOf(requestDto.totalOrderId()))
                .replace("{paymentPrice}", String.valueOf(requestDto.paymentPrice()))
                .replace("{orderAt}", requestDto.orderAt().toString())
                .replace("{orderStatus}", requestDto.orderStatus());
    }

    private NotificationTemplate getOrThrowNotificationTemplate(final NotificationTemplateType type) {
        return notificationTemplateRepository
                .findByType(type)
                .orElseThrow(NotificationException.NotificationTemplateNotFoundException::new);
    }
}

