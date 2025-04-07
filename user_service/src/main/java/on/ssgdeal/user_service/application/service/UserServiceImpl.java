package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.auth.enums.AuthRole;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.user_service.application.dto.CreateUserDto;
import on.ssgdeal.user_service.application.dto.GetSlackEmailByIdResponseDto;
import on.ssgdeal.user_service.application.dto.SearchUserDto;
import on.ssgdeal.user_service.application.dto.UpdateUserAdminDto;
import on.ssgdeal.user_service.application.dto.UpdateUserDto;
import on.ssgdeal.user_service.domain.entity.User;
import on.ssgdeal.user_service.domain.repository.UserRepository;
import on.ssgdeal.user_service.exception.UserException;
import on.ssgdeal.user_service.exception.UserExceptionCode;
import on.ssgdeal.user_service.presentation.external.dto.CreateUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.SearchUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserAdminResponse;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindMyUserResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j(topic = "UserServiceImpl")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PassportUtil passportUtil;
    @Value("${spring.cloud.gateway.auth.secret.key}")
    private String secretKey;

    @Override
    @Transactional
    public CreateUserResponse createUser(
        CreateUserDto dto,
        HttpServletRequest servletRequest
    ) {
        String headerSecretKey = servletRequest.getHeader("X-Internal-Secret");
        if (!headerSecretKey.equals(secretKey)) {
            throw new UserException(UserExceptionCode.USER_IS_NOT_ALLOWED);
        }

        User user = User.create(dto);

        User savedUser = userRepository.save(user);

        // TODO: User 생성 시 createdBy, updatedBy 넣어주기 위한 방법 필요
        //  savedUser.setId(savedUser.getId());

        return CreateUserResponse.from(savedUser);
    }

    @Override
    public FindByIdUserResponse findUserById(
        Long id,
        HttpServletRequest servletRequest
    ) {
        String PassportId = servletRequest.getHeader("X-Passport-Id");
        Passport passport = passportUtil.getPassportBy(PassportId);
        AuthRole role = AuthRole.valueOf(passport.getRole());
        if (!role.equals(AuthRole.MASTER)) {
            throw new UserException(UserExceptionCode.USER_IS_NOT_ALLOWED);
        }

        User user = findByIdOrElseThrow(id);
        return FindByIdUserResponse.from(user);
    }

    @Override
    public FindByIdUserResponse findUserByIdInternal(Long id, HttpServletRequest request) {
        String headerSecretKey = request.getHeader("X-Internal-Secret");
        if (!headerSecretKey.equals(secretKey)) {
            throw new UserException(UserExceptionCode.USER_IS_NOT_ALLOWED);
        }

        User user = findByIdOrElseThrow(id);
        return FindByIdUserResponse.from(user);
    }

    @Override
    public void withdrawUserByUserId(Long id, HttpServletRequest request) {
        log.info("withdrawUserByUserId, {}", id.toString());
        User user = findByIdOrElseThrow(id);

        userRepository.delete(user);
    }

    @Override
    public FindMyUserResponse findMyUser(HttpServletRequest request) {
        Passport passport = passportUtil.getPassportBy(request);
        Long id = passport.getUserId();

        User user = findByIdOrElseThrow(id);
        return FindMyUserResponse.from(user);
    }

    @Override
    public PageDto<SearchUserResponse> searchUser(SearchUserDto dto,
        HttpServletRequest servletRequest) {
        String PassportId = servletRequest.getHeader("X-Passport-Id");
        Passport passport = passportUtil.getPassportBy(PassportId);
        AuthRole role = AuthRole.valueOf(passport.getRole());
        if (!role.equals(AuthRole.MASTER)) {
            throw new UserException(UserExceptionCode.USER_IS_NOT_ALLOWED);
        }

        Page<User> users = userRepository.searchUser(dto);
        Page<SearchUserResponse> responsePage = users.map(SearchUserResponse::from);
        return PageDto.from(responsePage);
    }

    @Override
    @Transactional
    public UpdateUserResponse updateUser(UpdateUserDto dto, HttpServletRequest request) {
        Passport passport = passportUtil.getPassportBy(request);
        Long id = passport.getUserId();

        User user = findByIdOrElseThrow(id);

        user.updateNickname(dto.nickname());
        user.updateSlackEmail(dto.slackEmail());

        return UpdateUserResponse.from(user);
    }

    @Override
    @Transactional
    public UpdateUserAdminResponse updateUserAdmin(
        UpdateUserAdminDto dto,
        HttpServletRequest servletRequest
    ) {
        String PassportId = servletRequest.getHeader("X-Passport-Id");
        Passport passport = passportUtil.getPassportBy(PassportId);
        AuthRole role = AuthRole.valueOf(passport.getRole());
        if (!role.equals(AuthRole.MASTER)) {
            throw new UserException(UserExceptionCode.USER_IS_NOT_ALLOWED);
        }

        User user = findByIdOrElseThrow(dto.userId());

        user.updateNickname(dto.nickname());
        user.updateSlackEmail(dto.slackEmail());

        return UpdateUserAdminResponse.from(user);
    }


    @Override
    public GetSlackEmailByIdResponseDto getSlackEmailById(
        Long id,
        HttpServletRequest request
    ) {
        User user = findByIdOrElseThrow(id);
        return GetSlackEmailByIdResponseDto.from(user);
    }

    private User findByIdOrElseThrow(Long id) {
        return userRepository.findById(id).orElseThrow(
            () -> new UserException(UserExceptionCode.USER_IS_NOT_FOUND)
        );
    }


}
