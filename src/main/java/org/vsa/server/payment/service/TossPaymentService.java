package org.vsa.server.payment.service;

import org.vsa.server.payment.dto.PaymentRequest;
import org.vsa.server.payment.dto.PaymentResponse;
import org.vsa.server.payment.dto.TossPaymentRequest;
import org.vsa.server.payment.entity.Payment;
import org.vsa.server.payment.entity.PaymentType;
import org.vsa.server.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TossPaymentService implements PaymentInterface {

    private final PaymentRepository paymentRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${toss.secret_api_key}")
    private String secretKey;

    @Override
    public PaymentResponse processPayment(PaymentRequest paymentRequest, String authKey) {
        // 결제 정보 저장
        Payment payment = new Payment();
        payment.setPaymentType(PaymentType.TOSS);
        payment.setAmount(paymentRequest.amount());
        payment.setBuyerEmail(paymentRequest.buyerEmail());
        payment.setBuyerName(paymentRequest.buyerName());
        payment.setStatus("SUCCESS"); // 결제 성공으로 처리

        // 결제 요청 URL (Toss Payments API)
        String url = "https://api.tosspayments.com/v1/billing/" + paymentRequest.merchantUid() + "/pay";

        // Headers 설정
        var headers = new HttpHeaders();
        headers.setBasicAuth(secretKey, "");
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request 생성
        var request = new HttpEntity<>(new TossPaymentRequest(authKey), headers);

        try {
            // API 호출
            var response = restTemplate.postForEntity(url, request, PaymentResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                paymentRepository.save(payment);
                System.out.println("Toss Payments 응답: " + response.getBody());
                return response.getBody();
            } else {
                System.out.println("Toss Payments 응답 오류: " + response.getStatusCode());
                return new PaymentResponse(null, paymentRequest.merchantUid(), paymentRequest.amount(), false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new PaymentResponse(null, paymentRequest.merchantUid(), paymentRequest.amount(), false);
        }
    }
}
