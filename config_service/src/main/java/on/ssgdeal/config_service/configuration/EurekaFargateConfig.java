package on.ssgdeal.config_service.configuration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;

@Configuration
@Profile("aws")
public class EurekaFargateConfig {

    private static final String META_V4 = "ECS_CONTAINER_METADATA_URI_V4";
    private final RestTemplate rest;

    @Autowired
    public EurekaFargateConfig(RestTemplateBuilder builder) {
        this.rest = builder
            .connectTimeout(Duration.ofSeconds(2))
            .readTimeout(Duration.ofSeconds(2))
            .build();
    }

    @Bean
    public EurekaInstanceConfigBean eurekaInstanceConfigBean(
        InetUtils inetUtils,
        Environment env) throws Exception {

        String uri = System.getenv(META_V4);
        if (uri == null) {
            throw new IllegalStateException("ECS_CONTAINER_METADATA_URI_V4 환경변수 미설정");
        }

        String taskJson = rest.getForObject(uri + "/task", String.class);
        JsonNode root = new ObjectMapper().readTree(taskJson);
        JsonNode containers = root.path("Containers");

        JsonNode appContainer = null;
        for (JsonNode c : containers) {
            String name = c.path("Name").asText("");
            if (!name.contains("~internal~")) {
                appContainer = c;
                break;
            }
        }
        if (appContainer == null && !containers.isEmpty()) {
            appContainer = containers.get(0);
        }

        String ip = appContainer
            .path("Networks")
            .get(0)
            .path("IPv4Addresses")
            .get(0)
            .asText();

        int port = env.getProperty("server.port", Integer.class, 8080);
        JsonNode pm = appContainer.path("PortMappings");
        if (pm.isArray() && !pm.isEmpty()) {
            port = pm.get(0).path("HostPort").asInt(port);
        }

        EurekaInstanceConfigBean cfg = new EurekaInstanceConfigBean(inetUtils);
        String appName = env.getProperty("spring.application.name");
        cfg.setAppname(appName);
        cfg.setPreferIpAddress(true);
        cfg.setIpAddress(ip);
        cfg.setHostname(ip);
        cfg.setNonSecurePort(port);
        cfg.setNonSecurePortEnabled(true);

        cfg.setInstanceId(String.format("%s:%s:%d",
            ip, appName, port));

        return cfg;
    }
}