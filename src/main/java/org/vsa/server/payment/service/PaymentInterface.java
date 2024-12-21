package org.vsa.server.payment.service;

import org.vsa.server.payment.dto.PaymentRequest;
import org.vsa.server.payment.dto.PaymentResponse;

public interface PaymentInterface {
    PaymentResponse processPayment(PaymentRequest paymentRequest, String authKey);
}
