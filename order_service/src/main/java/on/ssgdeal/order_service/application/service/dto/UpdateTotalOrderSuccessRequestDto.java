package on.ssgdeal.order_service.application.service.dto;

import java.sql.Timestamp;
import on.ssgdeal.order_service.domain.enums.PaymentMethod;
import on.ssgdeal.order_service.domain.enums.PaymentStatus;
import on.ssgdeal.order_service.domain.enums.PaymentType;

public record UpdateTotalOrderSuccessRequestDto(Long totalOrderId,
                                                Long paymentId,
                                                PaymentType paymentType,
                                                PaymentMethod paymentMethod,
                                                Long paymentAmount,
                                                Timestamp paymentDate,
                                                String paymentKey,
                                                PaymentStatus paymentStatus) {

}
