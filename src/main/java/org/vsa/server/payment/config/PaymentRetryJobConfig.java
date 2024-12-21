package org.vsa.server.payment.config;

import org.vsa.server.common.FindLoginMember;
import org.vsa.server.payment.dto.PaymentRequest;
import org.vsa.server.payment.entity.Payment;
import org.vsa.server.payment.entity.PaymentStatus;
import org.vsa.server.payment.repository.PaymentRepository;
import org.vsa.server.payment.service.PaymentService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class PaymentRetryJobConfig {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private FindLoginMember findLoginMember;


    @Bean
    public Job retryPaymentJob(JobRepository jobRepository, Step retryPaymentStep) {
        return new JobBuilder("retryPaymentJob", jobRepository)
                .start(retryPaymentStep)
                .build();
    }

    @Bean
    public Step retryPaymentStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("retryPaymentStep", jobRepository)
                .tasklet(retryPaymentTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet retryPaymentTasklet() {
        return (contribution, chunkContext) -> {
            List<Payment> pendingPayments = paymentRepository.findAllByStatus(PaymentStatus.PENDING.name());
            System.out.println("Found " + pendingPayments.size() + " pending payments.");

            for (Payment payment : pendingPayments) {
                try {
                    PaymentRequest paymentRequest = new PaymentRequest(
                            payment.getPgProvider(),
                            payment.getPayMethod(),
                            payment.getMerchantUid(),
                            payment.getName(),
                            payment.getAmount(),
                            payment.getBuyerEmail(),
                            payment.getBuyerName(),
                            payment.getBuyerTel(),
                            payment.getBuyerAddr(),
                            payment.getBuyerPostcode(),
                            payment.getMember().getAuthKey(),
                            payment.getPaymentType()
                    );
                    paymentService.verifyAndSavePayment(paymentRequest, payment.getImpUid());
                    System.out.println("Payment verified and saved successfully for imp_uid: " + payment.getImpUid());
                } catch (Exception e) {
                    System.out.println("Payment retry failed for imp_uid: " + payment.getImpUid());
                    int attemptCount = payment.getRetryCount() + 1;
                    payment.setRetryCount(attemptCount);

                    if (attemptCount >= 3) {
                        paymentService.markPaymentAsFailed(payment);
                        System.out.println("Payment marked as UNPAID for imp_uid: " + payment.getImpUid());
                    } else {
                        payment.setStatus(PaymentStatus.PENDING.name());
                        paymentRepository.save(payment);
                        System.out.println("Payment status set to PENDING for imp_uid: " + payment.getImpUid());
                    }
                }
            }
            return RepeatStatus.FINISHED;
        };
    }
}
