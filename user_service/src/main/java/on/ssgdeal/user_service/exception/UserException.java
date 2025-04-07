package on.ssgdeal.user_service.exception;


import on.ssgdeal.common.global.exception.CustomException;
import on.ssgdeal.common.global.exception.ExceptionCode;

public class UserException extends CustomException {

    public UserException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }

}
