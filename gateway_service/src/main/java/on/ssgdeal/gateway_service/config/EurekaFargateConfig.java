package on.ssgdeal.gateway_service.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EurekaFargateConfig {

    private static final String META_V4 = "ECS_CONTAINER_METADATA_URI_V4";

    private final WebClient webClient;
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    public EurekaFargateConfig(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
            .baseUrl("") // 나중에 uri 전체 사용
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

        // 메타데이터를 WebClient로 가져오고 block()
        String taskJson = webClient
            .get()
            .uri(uri + "/task")
            .retrieve()
            .bodyToMono(String.class)
            .timeout(Duration.ofSeconds(2))
            .block();

        JsonNode root = mapper.readTree(taskJson);
        JsonNode containers = root.path("Containers");

        // 실제 앱 컨테이너 추출
        JsonNode appContainer = null;
        for (JsonNode c : containers) {
            if (!c.path("Name").asText("").contains("~internal~")) {
                appContainer = c;
                break;
            }
        }
        if (appContainer == null && containers.size() > 0) {
            appContainer = containers.get(0);
        }

        // IP
        String ip = appContainer
            .path("Networks").get(0)
            .path("IPv4Addresses").get(0)
            .asText();

        // PortMappings 없으면 server.port
        int port = env.getProperty("server.port", Integer.class, 8080);
        JsonNode pm = appContainer.path("PortMappings");
        if (pm.isArray() && pm.size() > 0) {
            port = pm.get(0).path("HostPort").asInt(port);
        }

        // Eureka 설정
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