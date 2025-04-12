package on.ssgdeal.order_service.application.service.dto;

public record CancelTotalOrderRequestDto(Long orderId, LoginUserInfoDto loginUserInfo) {

    public static CancelTotalOrderRequestDto from(Long orderId, LoginUserInfoDto loginUserInfo) {
        return new CancelTotalOrderRequestDto(orderId, loginUserInfo);
    }
}
