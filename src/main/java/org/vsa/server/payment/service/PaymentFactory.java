package org.vsa.server.payment.service;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentFactory {

    private final Map<String, PaymentInterface> paymentServiceMap;

    public PaymentFactory(Map<String, PaymentInterface> paymentServiceMap) {
        this.paymentServiceMap = paymentServiceMap;
    }

    public PaymentInterface getPaymentService(String pgProvider) {
        if (paymentServiceMap.containsKey(pgProvider)) {
            return paymentServiceMap.get(pgProvider);
        }
        throw new IllegalArgumentException("Unsupported payment provider: " + pgProvider);
    }
}
