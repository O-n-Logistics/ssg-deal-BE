package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.user_service.application.dto.CreateUserDto;
import on.ssgdeal.user_service.application.dto.GetSlackEmailByIdResponseDto;
import on.ssgdeal.user_service.application.dto.SearchUserDto;
import on.ssgdeal.user_service.application.dto.UpdateUserAdminDto;
import on.ssgdeal.user_service.application.dto.UpdateUserDto;
import on.ssgdeal.user_service.presentation.external.dto.CreateUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.SearchUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserAdminResponse;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindMyUserResponse;

public interface UserService {

    CreateUserResponse createUser(CreateUserDto requestDto, HttpServletRequest servletRequest);

    FindByIdUserResponse findUserById(Long id, HttpServletRequest servletRequest);

    FindMyUserResponse findMyUser(HttpServletRequest request);

    PageDto<SearchUserResponse> searchUser(SearchUserDto requestDto,
        HttpServletRequest servletRequest);

    UpdateUserResponse updateUser(UpdateUserDto requestDto, HttpServletRequest request);

    UpdateUserAdminResponse updateUserAdmin(UpdateUserAdminDto dto,
        HttpServletRequest servletRequest);

    FindByIdUserResponse findUserByIdInternal(Long id, HttpServletRequest request);

    void withdrawUserByUserId(Long id, HttpServletRequest request);

    GetSlackEmailByIdResponseDto getSlackEmailById(Long id, HttpServletRequest request);

}
