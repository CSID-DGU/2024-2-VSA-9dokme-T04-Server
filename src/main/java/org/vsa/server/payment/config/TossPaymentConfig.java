package org.vsa.server.payment.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class TossPaymentConfig {

    @Value("${toss.client_api_key}")
    private String testClientApiKey;

    @Value("${toss.secret_api_key}")
    private String testSecretKey;

    @Value("${toss.success_url}")
    private String successUrl;

    public String getTestSecretKey() {
        return testSecretKey;
    }

    @Value("${toss.fail_url}")
    private String failUrl;

    public static final String BASE_URL = "https://api.tosspayments.com/v1";
    public static final String BILLING_AUTH_URL = BASE_URL + "/billing/authorizations/issue";
}

