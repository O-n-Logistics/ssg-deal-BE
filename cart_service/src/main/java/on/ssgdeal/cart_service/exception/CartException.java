package on.ssgdeal.cart_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class CartException extends CustomException {

    public CartException(CartExceptionCode e) {
        super(e);
    }
}
