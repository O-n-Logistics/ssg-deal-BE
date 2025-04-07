package on.ssgdeal.user_service.application.dto;

import on.ssgdeal.user_service.domain.entity.User;

public record GetSlackEmailByIdResponseDto(
    String slackEmail
) {

    public static GetSlackEmailByIdResponseDto from(User user) {
        return new GetSlackEmailByIdResponseDto(
            user.getSlackEmail()
        );
    }
}
