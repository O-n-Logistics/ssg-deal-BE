package on.ssgdeal.user_service.presentation.external.dto.destination;

import on.ssgdeal.user_service.domain.entity.Destination;

public record UpdateMyDestinationResponse(
    Long destinationId
) {

    public static UpdateMyDestinationResponse from(Destination destination) {
        return new UpdateMyDestinationResponse(destination.getId());
    }
}
