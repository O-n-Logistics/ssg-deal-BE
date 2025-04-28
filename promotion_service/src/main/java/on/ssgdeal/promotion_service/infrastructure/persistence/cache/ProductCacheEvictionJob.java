package on.ssgdeal.promotion_service.infrastructure.persistence.cache;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
@Slf4j
public class ProductCacheEvictionJob extends QuartzJobBean {

    private ProductCacheService productCacheService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        Long productId = context.getMergedJobDataMap().getLong("productId");
        productCacheService.evictProductCache(productId);
    }

    @Autowired
    public void setProductCacheService(ProductCacheService productCacheService) {
        this.productCacheService = productCacheService;
    }
}
