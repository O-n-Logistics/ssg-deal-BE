package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import on.ssgdeal.user_service.presentation.external.dto.destination.GetMyDestinationsResponse;

public interface DestinationService {

    GetMyDestinationsResponse getMy(HttpServletRequest httpServletRequest);
}
