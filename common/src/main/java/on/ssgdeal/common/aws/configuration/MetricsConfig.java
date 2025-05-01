package on.ssgdeal.common.aws.configuration;

import io.micrometer.cloudwatch2.CloudWatchConfig;
import io.micrometer.cloudwatch2.CloudWatchMeterRegistry;
import io.micrometer.core.instrument.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.services.cloudwatch.CloudWatchAsyncClient;

@Configuration
public class MetricsConfig {

    @Bean
    public CloudWatchConfig cloudWatchConfig() {
        return key -> {
            if ("cloudwatch.namespace".equals(key)) {
                return "SSG-DEAL";
            }
            if ("cloudwatch.step".equals(key)) {
                return "PT1M";
            }
            return null;
        };
    }

    @Bean
    public CloudWatchMeterRegistry cloudWatchMeterRegistry(CloudWatchConfig cloudWatchConfig) {
        return new CloudWatchMeterRegistry(
            cloudWatchConfig,
            Clock.SYSTEM,
            CloudWatchAsyncClient.create()
        );
    }
}
