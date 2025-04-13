package on.ssgdeal.cart_service.infrastructure.persistence.repository;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public List<CartProduct> findAll(String key) {
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(key);
        return entries.entrySet().stream()
            .map(entry -> {
                String hashKey = (String) entry.getKey();
                Long quantity = (Long) entry.getValue();
                return CartProduct.create(hashKey, quantity);
            })
            .toList();
    }
}
