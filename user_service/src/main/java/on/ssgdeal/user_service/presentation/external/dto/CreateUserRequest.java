package on.ssgdeal.user_service.presentation.external.dto;

import on.ssgdeal.user_service.application.dto.CreateUserDto;
import on.ssgdeal.user_service.domain.vo.SlackEmail;

public record CreateUserRequest(
    String nickname,
    String slackEmail
) {

    public CreateUserDto toDto() {
        return new CreateUserDto(
            nickname,
            new SlackEmail(slackEmail)
        );
    }
}
