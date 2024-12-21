package org.vsa.server.payment.dto;

import org.vsa.server.payment.entity.PaymentType;

public record PaymentRequest(
        String pgProvider,
        String payMethod,
        String merchantUid,
        String name,
        int amount,
        String buyerEmail,
        String buyerName,
        String buyerTel,
        String buyerAddr,
        String buyerPostcode,
        String authKey,
        PaymentType paymentType
) {}