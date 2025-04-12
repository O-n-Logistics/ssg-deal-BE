package on.ssgdeal.promotion_service.exception;

import on.ssgdeal.common.global.exception.CustomException;

public class PromotionException extends CustomException {
    public PromotionException(PromotionExceptionCode promotionExceptionCode) {
        super(promotionExceptionCode);
    }
    public static class PromotionNotFoundException extends PromotionException {
        public PromotionNotFoundException() {
            super(PromotionExceptionCode.PROMOTION_NOT_FOUND);
        }
    }

}