package on.ssgdeal.user_service.presentation.external.dto;

import on.ssgdeal.user_service.application.dto.UpdateUserDto;
import on.ssgdeal.user_service.domain.vo.SlackEmail;

public record UpdateUserRequest(
    String nickname,
    String slackEmail
) {

    public UpdateUserDto toDto() {
        return new UpdateUserDto(
            nickname,
            new SlackEmail(slackEmail)
        );
    }

}
