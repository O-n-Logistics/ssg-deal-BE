package on.ssgdeal.cart_service.exception;

import lombok.Getter;
import on.ssgdeal.common.global.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

@Getter
public enum CartExceptionCode implements ExceptionCode {

    ;

    private final HttpStatus httpStatus;
    private final String message;

    CartExceptionCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
