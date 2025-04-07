package on.ssgdeal.user_service.application.dto;


import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.presentation.external.dto.CreateUserRequest;

public record CreateUserDto(
    String nickname,
    SlackEmail slackEmail
) {

    public static CreateUserDto from(CreateUserRequest request) {
        return new CreateUserDto(
            request.nickname(),
            new SlackEmail(request.slackEmail())
        );
    }
}
