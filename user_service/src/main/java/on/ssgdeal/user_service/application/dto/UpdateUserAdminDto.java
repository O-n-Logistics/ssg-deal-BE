package on.ssgdeal.user_service.application.dto;

import on.ssgdeal.user_service.domain.vo.SlackEmail;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserAdminRequest;

public record UpdateUserAdminDto(
    Long userId,
    String nickname,
    SlackEmail slackEmail
) {

    public static UpdateUserAdminDto from(
        Long userId,
        UpdateUserAdminRequest updateUserAdminRequest) {
        return new UpdateUserAdminDto(
            userId,
            updateUserAdminRequest.nickname(),
            new SlackEmail(updateUserAdminRequest.slackEmail())
        );
    }
}
