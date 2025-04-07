package on.ssgdeal.user_service.presentation.external.dto;

public record UpdateUserAdminRequest(
    String nickname,
    String slackEmail
) {

}
