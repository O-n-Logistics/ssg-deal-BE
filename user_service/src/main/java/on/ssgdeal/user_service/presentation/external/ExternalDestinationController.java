package on.ssgdeal.user_service.presentation.external;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.annotation.RoleCheck;
import on.ssgdeal.common.presentation.dto.CommonResponse;
import on.ssgdeal.user_service.application.service.DestinationService;
import on.ssgdeal.user_service.presentation.external.dto.destination.GetMyDestinationsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/destinations")
public class ExternalDestinationController {

    private final DestinationService destinationService;

    @RoleCheck("CONSUMER")
    @GetMapping("/my")
    public ResponseEntity<CommonResponse<GetMyDestinationsResponse>> getMy(
        HttpServletRequest httpServletRequest
    ) {
        GetMyDestinationsResponse response = destinationService.getMy(httpServletRequest);

        return ResponseEntity.ok(CommonResponse.success(response));
    }

}
