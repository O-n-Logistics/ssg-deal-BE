package on.ssgdeal.notification_service.presentation.internal.dto;

import jakarta.validation.constraints.NotBlank;
import on.ssgdeal.notification_service.domain.entity.Notification;

public record CreateNotificationResponse(
        @NotBlank Long notificationId
) {
    public static CreateNotificationResponse from(Notification notification) {
        return new CreateNotificationResponse(
                notification.getId()
        );
    }
}