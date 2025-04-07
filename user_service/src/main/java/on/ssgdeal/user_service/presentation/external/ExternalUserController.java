package on.ssgdeal.user_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.application.dto.PageDto;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.dto.CreateUserDto;
import on.ssgdeal.user_service.application.dto.GetSlackEmailByIdResponseDto;
import on.ssgdeal.user_service.application.dto.SearchUserDto;
import on.ssgdeal.user_service.application.dto.UpdateUserAdminDto;
import on.ssgdeal.user_service.application.dto.UpdateUserDto;
import on.ssgdeal.user_service.application.service.UserService;
import on.ssgdeal.user_service.presentation.external.dto.CreateUserRequest;
import on.ssgdeal.user_service.presentation.external.dto.CreateUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.SearchUserResponse;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserAdminRequest;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserAdminResponse;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserRequest;
import on.ssgdeal.user_service.presentation.external.dto.UpdateUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import on.ssgdeal.user_service.presentation.internal.dto.FindMyUserResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class ExternalUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<CommonResponse<CreateUserResponse>> createUser(
        @RequestBody CreateUserRequest request,
        HttpServletRequest servletRequest
    ) {
        CreateUserDto dto = CreateUserDto.from(request);
        CreateUserResponse response = userService.createUser(dto, servletRequest);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PatchMapping("/my")
    public ResponseEntity<CommonResponse<UpdateUserResponse>> updateUser(
        HttpServletRequest request,
        @RequestBody UpdateUserRequest updateUserRequest
    ) {
        UpdateUserDto dto = UpdateUserDto.from(updateUserRequest);
        UpdateUserResponse response = userService.updateUser(dto, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/my")
    public ResponseEntity<CommonResponse<FindMyUserResponse>> findMyUser(
        HttpServletRequest request
    ) {
        FindMyUserResponse response = userService.findMyUser(request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<FindByIdUserResponse>> findUserById(
        @PathVariable("id") Long id,
        HttpServletRequest request
    ) {
        FindByIdUserResponse response = userService.findUserById(id, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<PageDto<SearchUserResponse>>> getUser(
        @RequestParam(required = false) String nickname,
        @RequestParam(required = false) String slackEmail,
        @PageableDefault Pageable pageable,
        HttpServletRequest request
    ) {
        SearchUserDto dto = SearchUserDto.from(nickname, slackEmail, pageable);
        PageDto<SearchUserResponse> response = userService.searchUser(dto, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @GetMapping("/{id}/slack-email")
    public ResponseEntity<CommonResponse<GetSlackEmailByIdResponseDto>> getSlackEmailById(
        @PathVariable Long id,
        HttpServletRequest request
    ) {
        GetSlackEmailByIdResponseDto response = userService.getSlackEmailById(id, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PatchMapping("/{id}")
    private ResponseEntity<CommonResponse<UpdateUserAdminResponse>> updateUserAdmin(
        @PathVariable Long id,
        @RequestBody UpdateUserAdminRequest updateUserAdminRequest,
        HttpServletRequest request
    ) {
        UpdateUserAdminDto dto = UpdateUserAdminDto.from(id, updateUserAdminRequest);
        UpdateUserAdminResponse response = userService.updateUserAdmin(dto, request);
        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
