package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@DisplayName("장바구니 서비스 테스트")
class CartServiceImplTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final Long userId = 0L;
    private final Long productId = 0L;
    private final Long optionId = 0L;
    private final Long quantity = 5L;

    private final String key = RedisKeyGenerator.generateKey(userId);
    private final String hashKey = RedisKeyGenerator.generateHashKey(productId, optionId);

    @AfterEach
    void tearDown() {
        redisTemplate.delete(key);
    }
}