package on.ssgdeal.user_service.application.service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import on.ssgdeal.common.auth.passport.Passport;
import on.ssgdeal.common.auth.passport.PassportUtil;
import on.ssgdeal.user_service.domain.entity.Destination;
import on.ssgdeal.user_service.domain.repository.DestinationRepository;
import on.ssgdeal.user_service.exception.UserException;
import on.ssgdeal.user_service.exception.destination.DestinationExceptionCode;
import on.ssgdeal.user_service.presentation.external.dto.destination.GetMyDestinationsResponse;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DestinationServiceImpl implements DestinationService {

    private final DestinationRepository destinationRepository;
    private final PassportUtil passportUtil;

    @Override
    public GetMyDestinationsResponse getMy(HttpServletRequest httpServletRequest) {
        Passport passport = passportUtil.getPassportBy(httpServletRequest);
        List<Destination> myDestinations = destinationRepository.findByUserId(passport.getUserId());

        if (myDestinations.isEmpty()) {
            throw new UserException(DestinationExceptionCode.DESTINATION_NOT_FOUND);
        }

        return GetMyDestinationsResponse.from(
            passport.getNickname(),
            passport.getSlackEmail(),
            myDestinations
        );
    }
}
