package on.ssgdeal.user_service.presentation.external.dto;

import on.ssgdeal.user_service.application.dto.UpdateUserAdminDto;
import on.ssgdeal.user_service.domain.vo.SlackEmail;

public record UpdateUserAdminRequest(
    String nickname,
    String slackEmail
) {

    public UpdateUserAdminDto toDto(Long id) {
        return new UpdateUserAdminDto(
            id,
            nickname,
            new SlackEmail(slackEmail)
        );
    }

}
