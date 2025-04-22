package on.ssgdeal.notification_service.configuration;

import on.ssgdeal.notification_service.application.service.NotificationSender;
import on.ssgdeal.notification_service.domain.enums.NotificationChannelType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class NotificationSenderConfig {
    @Bean
    public Map<NotificationChannelType, NotificationSender> senderMap(
            List<NotificationSender> senders
    ) {
        return senders.stream()
                .collect(Collectors.toMap(
                        NotificationSender::getChannelType,
                        Function.identity()
                ));
    }
}
