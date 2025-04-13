package on.ssgdeal.promotion_service.application.service.dto;

import on.ssgdeal.promotion_service.domain.entity.dto.GetPromotionsDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionApplicationMapper {
    GetPromotionsResponseDto toResponseDto(GetPromotionsDto dto);
}
