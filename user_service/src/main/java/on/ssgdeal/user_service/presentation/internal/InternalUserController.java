package on.ssgdeal.user_service.presentation.internal;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.service.UserService;
import on.ssgdeal.user_service.presentation.internal.dto.FindByIdUserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j(topic = "InternalUserController")
@RequiredArgsConstructor
@RequestMapping("/internal/v1/users")
public class InternalUserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<FindByIdUserResponse>> findUserById(
        @PathVariable("id") Long id,
        HttpServletRequest request
    ) {

        FindByIdUserResponse response = userService.findUserByIdInternal(
            id,
            request
        );
        return ResponseEntity.ok(CommonResponse.success(response));
    }

    @PutMapping("/withdraw/{id}")
    public ResponseEntity<CommonResponse<Void>> withdrawUserByUserId(
        @PathVariable("id") Long id,
        HttpServletRequest request
    ) {
        log.info("withdrawUserByUserId, {}", id.toString());
        userService.withdrawUserByUserId(id, request);

        return ResponseEntity.ok(CommonResponse.success());
    }

}
