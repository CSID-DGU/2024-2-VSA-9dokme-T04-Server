package org.vsa.server.payment.dto;

public record PaymentResponse(
        String customerUid,
        String merchantUid,
        int paidAmount,
        boolean success
) {}
