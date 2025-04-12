package on.ssgdeal.payment_service.infrastructure.client.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.payment_service.application.service.OrderService;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.OrderServiceFeignClient;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.CreateOrderPaymentSuccessRequestDto;
import on.ssgdeal.payment_service.infrastructure.client.order.feign.dto.GetValidTotalOrderId;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "OrderServiceImpl")
public class OrderServiceImpl implements OrderService {

    private final OrderServiceFeignClient orderServiceFeignClient;

    @Override
    public GetValidTotalOrderId getValidTotalOrderId(Long totalOrderId) {
        var getValidTotalOrderDto = orderServiceFeignClient.getValidTotalOrderId(totalOrderId);
        return getValidTotalOrderDto.data();
    }

    @Override
    public void createOrderPaymentSuccess(CreateOrderPaymentSuccessRequestDto requestDto) {
        log.info("createOrderPaymentSuccess : {}", requestDto.toString());
        orderServiceFeignClient.createOrderPaymentSuccess(requestDto);
    }
}
