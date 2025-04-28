package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import lombok.extern.slf4j.Slf4j;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class AutowiringSpringBeanJobFactory extends SpringBeanJobFactory
    implements ApplicationContextAware {

    private AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(@NonNull final TriggerFiredBundle bundle) throws Exception {
        try {
            final Object job = super.createJobInstance(bundle);
            beanFactory.autowireBean(job);
            return job;
        } catch (Exception e) {
            log.info("createJobInstance error : {}", e.getMessage());
            throw e;
        }
    }
}

