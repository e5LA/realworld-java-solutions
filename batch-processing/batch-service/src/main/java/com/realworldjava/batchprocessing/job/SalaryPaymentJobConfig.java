package com.realworldjava.batchprocessing.job;

import com.realworldjava.batchprocessing.entity.Payment;
import com.realworldjava.batchprocessing.entity.Timesheet;
import com.realworldjava.batchprocessing.repository.PaymentRepository;
import com.realworldjava.batchprocessing.repository.TimesheetRepository;
import java.util.List;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SalaryPaymentJobConfig {

  private static final String JOB_NAME = "salaryPaymentJob";
  private static final String STEP_NAME = "salaryPaymentStep";

  @Bean
  public Job salaryPaymentJob(JobRepository jobRepository, Step salaryPaymentStep) {
    return new JobBuilder(JOB_NAME, jobRepository)
        .start(salaryPaymentStep)
        .build();
  }

  @Bean
  public Step salaryPaymentStep(JobRepository jobRepository,
      PlatformTransactionManager transactionManager,
      RepositoryItemReader<Timesheet> reader,
      ItemProcessor<Timesheet, Payment> processor,
      ItemWriter<Payment> writer) {

    return new StepBuilder(STEP_NAME, jobRepository)
        .<Timesheet, Payment>chunk(10, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @Bean
  public RepositoryItemReader<Timesheet> reader(TimesheetRepository repository) {
    return new RepositoryItemReaderBuilder<Timesheet>()
        .name("timesheetRepoReader")
        .repository(repository)
        .methodName("findByApprovedIsTrueAndProcessedIsFalse")
        .arguments(List.of()) // Pageable auto-injected
        .pageSize(10)
        .sorts(Map.of("id", Direction.ASC))
        .build();
  }

  @Bean
  public ItemWriter<Payment> writer(PaymentRepository repository) {
    return repository::saveAll;
  }

}
