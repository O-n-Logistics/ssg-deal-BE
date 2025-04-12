package on.ssgdeal.promotion_service.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum PromotionExceptionCode implements ExceptionCode {

    PROMOTION_NOT_FOUND(HttpStatus.NOT_FOUND, "프로모션을 찾을 수 없습니다."),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
