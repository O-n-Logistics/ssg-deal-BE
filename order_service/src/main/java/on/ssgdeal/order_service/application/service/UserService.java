package on.ssgdeal.order_service.application.service;

import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationRequestDto;
import on.ssgdeal.order_service.infrastructure.client.user.feign.dto.ValidDestinationResponseDto;

public interface UserService {

    ValidDestinationResponseDto validDestinationRequest(ValidDestinationRequestDto dto);
}
