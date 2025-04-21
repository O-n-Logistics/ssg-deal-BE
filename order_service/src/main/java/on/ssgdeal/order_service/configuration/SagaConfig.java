package on.ssgdeal.order_service.configuration;

import on.ssgdeal.common.command.ScopedCommandInvoker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SagaConfig {
    
    /**
     * Spring Bean으로 등록되는 ScopedCommandInvoker 인스턴스를 생성합니다.
     *
     * @return ScopedCommandInvoker의 새 인스턴스
     */
    @Bean
    public ScopedCommandInvoker scopedCommandInvoker() {
        return new ScopedCommandInvoker();
    }

}
