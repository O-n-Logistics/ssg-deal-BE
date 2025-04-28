package on.ssgdeal.promotion_service.configuration;

import on.ssgdeal.promotion_service.infrastructure.persistence.cache.AutowiringSpringBeanJobFactory;
import on.ssgdeal.promotion_service.infrastructure.persistence.cache.ProductCacheEvictionJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
public class QuartzConfig {

    @Bean
    public JobDetailFactoryBean jobDetail() {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ProductCacheEvictionJob.class);
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setApplicationContextJobDataKey("applicationContext");
        jobDetailFactory.setJobDataMap(new JobDataMap());
        return jobDetailFactory;
    }

    @Bean
    public CronTriggerFactoryBean trigger(JobDetail jobDetail) {
        CronTriggerFactoryBean cronTriggerFactory = new CronTriggerFactoryBean();
        cronTriggerFactory.setJobDetail(jobDetail);
        cronTriggerFactory.setCronExpression("0 0 0 * * ?");
        return cronTriggerFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler(
        AutowiringSpringBeanJobFactory jobFactory
    ) {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();

        schedulerFactory.setOverwriteExistingJobs(true);
        schedulerFactory.setStartupDelay(0);

        schedulerFactory.setJobFactory(jobFactory);
        return schedulerFactory;
    }

    @Bean
    public AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory(
        ApplicationContext applicationContext
    ) {
        AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
}
