package org.vsa.server.payment.service;

import org.vsa.server.payment.dto.PaymentRequest;
import org.vsa.server.payment.dto.PaymentResponse;
import org.springframework.stereotype.Component;

@Component
public class KakaoPaymentService implements PaymentInterface {

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest, String authKey) {
        return new PaymentResponse(
                paymentRequest.merchantUid(),
                paymentRequest.merchantUid(),
                paymentRequest.amount(),
                true
        );
    }
}