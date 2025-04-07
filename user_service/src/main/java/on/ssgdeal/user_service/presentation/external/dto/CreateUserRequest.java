package on.ssgdeal.user_service.presentation.external.dto;

public record CreateUserRequest(
    String nickname,
    String slackEmail
) {
 
}
