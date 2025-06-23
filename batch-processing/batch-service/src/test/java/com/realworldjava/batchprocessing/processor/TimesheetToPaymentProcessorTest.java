package com.realworldjava.batchprocessing.processor;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.realworldjava.batchprocessing.entity.Payment;
import com.realworldjava.batchprocessing.entity.Timesheet;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TimesheetToPaymentProcessorTest {

  private TimesheetToPaymentProcessor processor;

  @BeforeEach
  void setUp() {
    processor = new TimesheetToPaymentProcessor();
  }

  @Test
  void shouldCorrectlyCalculatePayment() {
    // given
    var timesheet = new Timesheet();
    timesheet.setEmployeeId(1001);
    timesheet.setHours(new BigDecimal("160"));
    timesheet.setOvertime(new BigDecimal("10"));
    timesheet.setDeductions(new BigDecimal("200.00"));

    // when
    Payment result = processor.process(timesheet);

    // then
    assertEquals(1001, result.getEmployeeId(), "Employee ID should match");
    assertEquals(new BigDecimal("3300.00"),result.getAmount(), "Total should be greater than zero");
  }

}
