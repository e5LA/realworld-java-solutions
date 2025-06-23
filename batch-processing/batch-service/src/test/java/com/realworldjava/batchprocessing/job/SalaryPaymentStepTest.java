package com.realworldjava.batchprocessing.job;

import static org.assertj.core.api.Assertions.assertThat;

import com.realworldjava.batchprocessing.entity.Payment;
import com.realworldjava.batchprocessing.entity.Timesheet;
import com.realworldjava.batchprocessing.repository.PaymentRepository;
import com.realworldjava.batchprocessing.repository.TimesheetRepository;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBatchTest
@SpringBootTest
class SalaryPaymentStepTest {
  @Autowired
  private JobLauncherTestUtils jobLauncherTestUtils;

  @Autowired
  private TimesheetRepository timesheetRepository;

  @Autowired
  private PaymentRepository paymentRepository;

  @Test
  void salaryPaymentStep_shouldProcessApprovedTimesheets() {
    // given
    timesheetRepository.saveAll(List.of(
        createTimesheet(100, true, false),
        createTimesheet(101, true, false),
        createTimesheet(102, false, false), // should be ignored
        createTimesheet(103, true, true)    // already processed
    ));

    // when
    JobExecution execution = jobLauncherTestUtils.launchStep("salaryPaymentStep");

    // then
    assertThat(execution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

    List<Payment> payments = paymentRepository.findAll();
    assertThat(payments).hasSize(2);
    assertThat(payments).extracting(Payment::getEmployeeId)
        .containsExactlyInAnyOrder(100, 101);
  }

  private Timesheet createTimesheet(int employeeId, boolean approved, boolean processed) {
    var timesheet = new Timesheet();
    timesheet.setEmployeeId(employeeId);
    timesheet.setApproved(approved);
    timesheet.setProcessed(processed);
    timesheet.setHours(new BigDecimal("160"));
    timesheet.setOvertime(new BigDecimal("10"));
    timesheet.setDeductions(new BigDecimal("100.00"));
    return timesheet;
  }
}
