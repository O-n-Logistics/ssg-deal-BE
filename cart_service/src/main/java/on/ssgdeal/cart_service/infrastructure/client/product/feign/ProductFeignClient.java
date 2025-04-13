package on.ssgdeal.cart_service.infrastructure.client.product.feign;

import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsRequest;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductDetailsResponse;
import on.ssgdeal.cart_service.infrastructure.client.product.feign.dto.GetProductOptionsResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "product-service")
public interface ProductFeignClient {

    @PostMapping("/internal/v1/promotions/products/detail")
    GetProductDetailsResponse getProductDetails(@RequestBody GetProductDetailsRequest request);

    @GetMapping("/api/v1/promotions/products/options/list")
    GetProductOptionsResponse getProductOptions(@RequestParam Long productId);
}
