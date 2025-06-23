package com.realworldjava.batchprocessing.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobScheduler {
  private static final Logger log = LoggerFactory.getLogger(JobScheduler.class);

  private final JobLauncher jobLauncher;
  private final Job salaryPaymentJob;

  public JobScheduler(JobLauncher jobLauncher, Job salaryPaymentJob) {
    this.jobLauncher = jobLauncher;
    this.salaryPaymentJob = salaryPaymentJob;
  }

  @Scheduled(fixedRate = 60_000) // every 60 seconds
  public void runJob() {
    try {
      JobParameters params = new JobParametersBuilder()
          .addLong("timestamp", System.currentTimeMillis())
          .toJobParameters();

      log.info("Starting batch job: {}", salaryPaymentJob.getName());

      JobExecution execution = jobLauncher.run(salaryPaymentJob, params);

      log.info("Job finished. Status: {}", execution.getStatus());
    } catch (Exception e) {
      log.error("Error executing batch job", e);
    }
  }
}
