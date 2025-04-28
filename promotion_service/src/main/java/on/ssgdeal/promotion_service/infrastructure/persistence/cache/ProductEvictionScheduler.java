package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.promotion_service.exception.ProductException.ProductEvictScheduleRegistrationFailedException;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductEvictionScheduler {

    private final Scheduler scheduler;

    public void scheduleEviction(Long productId, LocalDateTime endTime, int delay) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(ProductCacheEvictionJob.class)
                .withIdentity("evict_" + productId, "product")
                .usingJobData("productId", productId)
                .build();

            ZonedDateTime triggerTime = endTime.plusSeconds(delay)
                .atZone(ZoneId.systemDefault());

            Trigger trigger = TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity("trigger_" + productId, "product")
                .startNow() //startAt , withSchedule, with MisfireHandlingInstructionFireNow, build
                .startAt(Date.from(triggerTime.toInstant()))
                .withSchedule(simpleSchedule()
                    .withMisfireHandlingInstructionFireNow()
                    .withIntervalInSeconds(40)
                    .repeatForever())
                .build();

            scheduler.scheduleJob(jobDetail, trigger);
            log.info("Product cache eviction job scheduled for productId={} at {}",
                productId, triggerTime);
        } catch (SchedulerException e) {
            log.info("Failed to schedule eviction for productId={}: {}", productId, e.getMessage());
            throw new ProductEvictScheduleRegistrationFailedException();
        }
    }
}