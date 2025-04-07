package on.ssgdeal.user_service.application.dto;

import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserRequest;

public record UpdateUserDto(
    String nickname,
    SlackEmail slackEmail
) {

    public static UpdateUserDto from(UpdateUserRequest request) {
        return new UpdateUserDto(
            request.nickname(),
            new SlackEmail(request.slackEmail())
        );
    }
}
