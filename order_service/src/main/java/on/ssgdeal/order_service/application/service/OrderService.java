package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.application.service.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.service.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.presentation.external.dto.CreateOrderResponse;

public interface OrderService {

    CreateOrderResponse createOrder(CreateOrderRequestDto request, LoginUserInfoDto loginUserInfo);
}
