package on.ssgdeal.order_service.application.service.dto;

import on.ssgdeal.common.auth.passport.Passport;

public record LoginUserInfoDto(Long userId, String username, String nickname,
                               String slackEmail) {

    public static LoginUserInfoDto from(Passport passport) {
        return new LoginUserInfoDto(passport.getUserId(), passport.getUsername(),
            passport.getNickname(), passport.getSlackEmail());
    }

    public static LoginUserInfoDto testMethod(Long userId, String username,
        String nickname,
        String slackEmail) {
        return new LoginUserInfoDto(userId, username, nickname, slackEmail);
    }
}
