package org.vsa.server.springbatch;

import org.vsa.server.payment.service.PaymentService;
import org.vsa.server.subscribe.entity.PaymentStatus;
import org.vsa.server.subscribe.entity.Subscribe;
import org.vsa.server.subscribe.repository.SubscribeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;

@Configuration
@EnableBatchProcessing
public class SubscribeBatchConfig {

    @Autowired
    JobRepository jobRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private SubscribeRepository subscribeRepository;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    PaymentService paymentService;

    @Scheduled(cron= "0 0 10 * * *")   //오전 10시 수정 $$$
    public void performJob() throws Exception {
        jobLauncher.run(subscribeBatchJob(), new JobParameters());
    }

    //job
    @Bean
    public Job subscribeBatchJob() {
        return new JobBuilder("subscribeBatchJob", jobRepository)
                .start(updateSubscribe())
                .build();
    }

    //step stepA - 전체 subscribe 읽고
    @Bean
    public Step updateSubscribe(){
        return new StepBuilder("updateSubscribe", jobRepository)
                .<Subscribe, Subscribe>chunk(10, transactionManager)
                .reader(subscribeItemReader())
                .processor(subscribeItemProcessor())
                .writer(subscribeItemWriter())
                .build();
    }

    //reader(subscribe)
    @Bean
    public ItemReader<Subscribe> subscribeItemReader() {
        return new RepositoryItemReaderBuilder<Subscribe>()
                .repository(subscribeRepository).methodName("findExpiredSubscriptions")
                .arguments(LocalDate.now(ZoneId.of("Asia/Seoul")), PaymentStatus.PAID)
                .pageSize(10)
                .sorts(Collections.singletonMap("subscribeId", Sort.Direction.ASC))
                .name("subscribeItemReader")
                .build();
    }

    @Bean
    public ItemProcessor<Subscribe, Subscribe> subscribeItemProcessor() {
        return subscribe -> {
            subscribe.setCreatedAt(LocalDate.now());
            subscribe.setExpiredAt(LocalDate.now().plusMonths(1));
            return subscribe;
        };
    }

    //writer(subscribe)
    @Bean
    public ItemWriter<Subscribe> subscribeItemWriter() {
        return subscribes -> {

            subscribeRepository.saveAll(subscribes);
        };
    }

    



}
