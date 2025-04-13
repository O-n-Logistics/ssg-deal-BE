package on.ssgdeal.cart_service.application.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;
import on.ssgdeal.cart_service.domain.entity.CartProduct;
import on.ssgdeal.cart_service.domain.repository.CartRepository;
import on.ssgdeal.cart_service.infrastructure.client.product.ProductServiceImpl.GetProductOptionsResponseDto;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductOptionsResponse;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.infrastructure.persistence.generator.RedisKeyGenerator;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "cart-service")
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final ProductService productService;

    @Override
    public GetProductsByIdsResponseDto getCarts(Long userId) {
        log.info("getCarts userId: {}", userId);

        String key = RedisKeyGenerator.generateKey(userId);
        List<CartProduct> cartProducts = cartRepository.findAll(key);
        log.info("getCarts cartProducts: {}", cartProducts);

        List<GetProductDetailsResponse> detailsResponseList =
            productService.getProductsByHashKeys(cartProducts);
        List<GetProductOptionsResponseDto> productOptionsResponses =
            productService.getProductOptions(cartProducts);

        return GetProductsByIdsResponseDto.from(
            detailsResponseList, productOptionsResponses, cartProducts);
    }
}
