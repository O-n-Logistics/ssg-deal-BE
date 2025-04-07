package on.ssgdeal.user_service.application.dto;

import org.springframework.data.domain.Pageable;

public record SearchUserDto(
    String nickname,
    String slackEmail,
    Pageable pageable
) {
    public static SearchUserDto from(
        String nickname,
        String slackEmail,
        Pageable pageable
    ) {
        return new SearchUserDto(
            nickname,
            slackEmail,
            pageable);
    }

}
