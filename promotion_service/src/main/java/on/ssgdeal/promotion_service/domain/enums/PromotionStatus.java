package on.ssgdeal.promotion_service.domain.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum PromotionStatus {

    PENDING("예정"),
    IN_PROGRESS("진행 중"),
    FINISHED("종료"),
    ;
    private final String description;
    public static PromotionStatus from(String value) {
        return Arrays.stream(PromotionStatus.values())
                .filter(status -> status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 프로모션 상태 값입니다."));
    }
}
