package on.ssgdeal.promotion_service.application.service.dto;

import lombok.Builder;
import on.ssgdeal.promotion_service.domain.entity.dto.GetCompaniesConditionDto;
import org.springframework.data.domain.Pageable;

@Builder
public record GetCompaniesRequestDto(
        String keyword,
        Pageable pageable
) {
    public static GetCompaniesConditionDto toDto(GetCompaniesRequestDto requestDto) {
        return GetCompaniesConditionDto.builder()
                .keyword(requestDto.keyword())
                .pageable(requestDto.pageable())
                .build();
    }
}
