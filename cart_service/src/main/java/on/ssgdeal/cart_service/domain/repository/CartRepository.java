package on.ssgdeal.cart_service.domain.repository;

import java.util.List;
import on.ssgdeal.cart_service.domain.entity.CartProduct;

public interface CartRepository {

    List<CartProduct> findAll(String key);
}
