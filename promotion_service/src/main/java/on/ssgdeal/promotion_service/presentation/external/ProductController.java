package on.ssgdeal.promotion_service.presentation.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.annotation.RoleCheck;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.application.dto.SliceDto;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.promotion_service.application.service.ProductService;
import on.ssgdeal.promotion_service.application.service.dto.product.FindProductByPromotionIdRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.UpdateOptionRequestDto;
import on.ssgdeal.promotion_service.application.service.dto.product.UpdateProductRequestDto;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.FindByPromotionIdResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.GetProductRankingResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.SearchProductResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateOptionRequest;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateOptionResponse;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateProductRequest;
import on.ssgdeal.promotion_service.presentation.external.dto.product.UpdateProductResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/promotions")
@Slf4j(topic = "ProductController")
public class ProductController {

    private final ProductService productService;

    @GetMapping("/products/search")
    public ResponseEntity<CommonResponse<PageDto<SearchProductResponse>>> search(
        @RequestParam(required = false, defaultValue = "", name = "productName") String productName,
        @PageableDefault Pageable pageable
    ) {
        log.info("Search products for {}", productName);
        PageDto<SearchProductResponse> response = productService.searchWithProductName(
            productName, pageable);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/all/products/{productId}")
    public ResponseEntity<CommonResponse<FindByIdResponse>> findById(
        @PathVariable(name = "productId") Long productId
    ) {
        log.info("Find product by id {}", productId);
        long start = System.currentTimeMillis();

        FindByIdResponse response = productService.findById(productId);

        long end = System.currentTimeMillis();
        log.info("findById({}) execution time: {} ms", productId, (end - start));

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/all/products/{productId}/cache")
    public ResponseEntity<CommonResponse<FindByIdResponse>> findByIdCache(
        @PathVariable(name = "productId") Long productId
    ) {
        log.info("Find product by id {}", productId);
        long start = System.currentTimeMillis();

        FindByIdResponse response = productService.findByIdCache(productId);

        long end = System.currentTimeMillis();
        log.info("findByIdCache({}) execution time: {} ms", productId, (end - start));

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/all/products/{productId}/non_cache")
    public ResponseEntity<CommonResponse<FindByIdResponse>> findByIdNonCache(
        @PathVariable(name = "productId") Long productId
    ) {
        log.info("Find product by id {}", productId);
        long start = System.currentTimeMillis();

        FindByIdResponse response = productService.findByIdNonCache(productId);

        long end = System.currentTimeMillis();
        log.info("findByIdNonCache({}) execution time: {} ms", productId, (end - start));

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{promotionId}/products")
    public ResponseEntity<CommonResponse<SliceDto<FindByPromotionIdResponse>>> findByPromotionId(
        @PathVariable(name = "promotionId") Long promotionId,
        @PageableDefault Pageable pageable
    ) {
        log.info("Find product by promotion id {}, {}", promotionId, pageable);
        long start = System.currentTimeMillis();

        FindProductByPromotionIdRequestDto dto = FindProductByPromotionIdRequestDto.from(
            promotionId,
            pageable
        );

        SliceDto<FindByPromotionIdResponse> response = productService.findByPromotionId(dto);

        long end = System.currentTimeMillis();
        log.info("findByPromotionId({}) execution time: {} ms", promotionId, (end - start));

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{promotionId}/products/non_cache")
    public ResponseEntity<CommonResponse<SliceDto<FindByPromotionIdResponse>>> findByPromotionIdNonCache(
        @PathVariable(name = "promotionId") Long promotionId,
        @PageableDefault Pageable pageable
    ) {
        log.info("Find product by promotion id {}, {}", promotionId, pageable);
        long start = System.currentTimeMillis();

        FindProductByPromotionIdRequestDto dto = FindProductByPromotionIdRequestDto.from(
            promotionId,
            pageable
        );

        SliceDto<FindByPromotionIdResponse> response = productService.findByPromotionIdNonCache(
            dto);

        long end = System.currentTimeMillis();
        log.info("findByPromotionIdNonCache({}) execution time: {} ms", promotionId, (end - start));

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/products/rankings")
    public ResponseEntity<CommonResponse<GetProductRankingResponse>> getProductRanking() {
        log.info("Get product rankings");
        GetProductRankingResponse response = productService.getProductRanking();

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck("MASTER")
    @PatchMapping("/products/{productId}")
    public ResponseEntity<CommonResponse<UpdateProductResponse>> updateProduct(
        @PathVariable(name = "productId") Long productId,
        @RequestBody UpdateProductRequest request
    ) {
        log.info("Update product {}, {}", productId, request);

        UpdateProductRequestDto dto = UpdateProductRequestDto.from(productId, request);
        UpdateProductResponse response = productService.updateProduct(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @RoleCheck("MASTER")
    @PatchMapping("/products/options/{optionId}")
    public ResponseEntity<CommonResponse<UpdateOptionResponse>> updateOption(
        @PathVariable(name = "optionId") Long optionId,
        @RequestBody UpdateOptionRequest request
    ) {
        log.info("Update option {}, {}", optionId, request);

        UpdateOptionRequestDto dto = UpdateOptionRequestDto.from(optionId, request);
        UpdateOptionResponse response = productService.updateOption(dto);

        return ResponseEntity.ok(CommonResponse.success(response));
    }


}
