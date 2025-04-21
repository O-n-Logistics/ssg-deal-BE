package on.ssgdeal.order_service.application.command;

import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.command.CommandFactory;
import on.ssgdeal.order_service.application.command.support.OrderCommandSupport;
import on.ssgdeal.order_service.application.dto.CreateOrderRequestDto;
import on.ssgdeal.order_service.application.dto.LoginUserInfoDto;
import on.ssgdeal.order_service.domain.repository.TotalOrderRepository;
import on.ssgdeal.order_service.util.OrderNumberGenerator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCommandFactory implements CommandFactory {

    private final TotalOrderRepository totalOrderRepository;
    private final OrderNumberGenerator orderNumberGenerator;
    private final OrderCommandSupport support;

    /**
     * 새로운 주문 생성을 위한 CreateOrderCommand 인스턴스를 생성합니다.
     *
     * @param request 주문 생성 요청 정보가 담긴 DTO
     * @param loginUserInfoDto 로그인한 사용자 정보가 담긴 DTO
     * @return 생성된 CreateOrderCommand 인스턴스
     */
    public CreateOrderCommand createOrderCommand(
        CreateOrderRequestDto request,
        LoginUserInfoDto loginUserInfoDto
    ) {
        return new CreateOrderCommand(
            request,
            loginUserInfoDto,
            totalOrderRepository,
            orderNumberGenerator,
            support
        );
    }
}

