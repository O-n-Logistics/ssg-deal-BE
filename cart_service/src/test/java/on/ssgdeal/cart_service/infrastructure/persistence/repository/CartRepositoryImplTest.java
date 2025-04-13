package on.ssgdeal.cart_service.infrastructure.persistence.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@DisplayName("장바구니 레포지토리 테스트")
class CartRepositoryImplTest {

    @Autowired
    private CartRepositoryImpl cartRepository;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private final String key = "cart:0";
    private final String hashKey = "product:0";
    private final Long quantity = 5L;

    @AfterEach
    void tearDown() {
        redisTemplate.delete(key);
    }
}
