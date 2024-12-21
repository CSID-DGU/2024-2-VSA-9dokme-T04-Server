package org.vsa.server.payment.controller;

import org.vsa.server.common.FindLoginMember;
import org.vsa.server.payment.dto.PaymentRequest;
import org.vsa.server.payment.dto.WebhookRequest;
import org.vsa.server.payment.entity.PaymentType;
import org.vsa.server.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Value("${iamport.merchant.code}")
    private String iamportMerchantCode;

    @Value("${iamport.pg.cid}")
    private String iamportPgCid;


    @GetMapping("/payments")
    public String paymentPage(Model model) {

        PaymentRequest paymentRequest = new PaymentRequest(
                "kakaopay",
                "card",
                "order_no_" + new Date().getTime(),
                "9dokme 정기 결제",
                9900,
                "jenny.seulgi.baek@gmail.com",
                "백슬기",
                "010-5511-0021",
                "서울특별시",
                "123-456",
                "bln_ZaQg06E4eOA",
                PaymentType.KAKAO
        );

        // 사용자 정보를 모델에 추가
        model.addAttribute("userEmail", "jenny.seulgi.baek@gmail.com");
        model.addAttribute("userName", "백슬기");

        model.addAttribute("paymentRequest", paymentRequest);

        return "payment";
    }

    @PostMapping("/payments/complete")
    @ResponseBody
    public ResponseEntity<String> completePayment(@RequestBody PaymentRequest paymentRequest, @RequestParam(required = false) String imp_uid) {

        String memberEmail = FindLoginMember.getCurrentUserId();


        try {
            paymentService.verifyAndSavePayment(paymentRequest, imp_uid, memberEmail);
            return ResponseEntity.ok("Payment verified and saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Payment verification failed: " + e.getMessage());
        }
    }

    // 웹훅을 통해 결제 완료를 통보받는 경우를 위한 엔드포인트
    @PostMapping("/payments/webhook")
    @ResponseBody
    public ResponseEntity<String> paymentWebhook(@RequestBody WebhookRequest webhookRequest) {
        String impUid = webhookRequest.impUid();
        try {
            // impUid를 사용하여 결제 정보를 조회하고 처리
            return ResponseEntity.ok("Webhook received and processed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Webhook processing failed: " + e.getMessage());
        }
    }
}
