package on.ssgdeal.user_service.presentation.external.dto;

public record UpdateUserRequest(
    String nickname,
    String slackEmail
) {

}
