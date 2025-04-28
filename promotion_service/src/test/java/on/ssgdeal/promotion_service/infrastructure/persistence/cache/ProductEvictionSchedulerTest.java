package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
class ProductEvictionSchedulerTest {

    @Autowired
    private Scheduler scheduler;

    @Autowired
    private ProductEvictionScheduler productEvictionScheduler;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @BeforeEach
    void cleanScheduler() throws Exception {
        scheduler.clear();
        if (!scheduler.isStarted()) {
            scheduler.start();
        }
    }

    @Test
    void cache_should_be_evicted_and_job_registered() throws Exception {
        Long productId = System.nanoTime();
        String redisKey = "promotion:product:" + productId + ":v1";
        redisTemplate.opsForValue().set(redisKey, "data");

        log.info("redis has key: {}", redisTemplate.hasKey(redisKey));
        Thread.sleep(10000);

        LocalDateTime now = LocalDateTime.now().plusSeconds(2);
        productEvictionScheduler.scheduleEviction(productId, now, 0);

        JobKey jobKey = JobKey.jobKey("evict_" + productId, "product");

        Awaitility.await().atMost(Duration.ofSeconds(8))
            .untilAsserted(
                () -> assertThat(redisTemplate.hasKey(redisKey)).isFalse());

        Awaitility.await().atMost(Duration.ofSeconds(2))
            .untilAsserted(() ->
                assertThat(
                    scheduler.checkExists(jobKey)
                ).isTrue());
    }
}