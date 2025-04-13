package on.ssgdeal.cart_service.application.service;

import on.ssgdeal.cart_service.application.service.dto.GetProductsByIdsResponseDto;

public interface CartService {

    GetProductsByIdsResponseDto getCarts(Long userId);
}
