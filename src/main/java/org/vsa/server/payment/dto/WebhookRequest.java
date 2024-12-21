package org.vsa.server.payment.dto;

public record WebhookRequest(String impUid, String merchantUid) {
}
