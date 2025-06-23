package com.realworldjava.batchprocessing.processor;

import com.realworldjava.batchprocessing.entity.Payment;
import com.realworldjava.batchprocessing.entity.Timesheet;
import java.math.BigDecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TimesheetToPaymentProcessor implements ItemProcessor<Timesheet, Payment> {
  private static final Logger log = LoggerFactory.getLogger(TimesheetToPaymentProcessor.class);

  @Override
  public Payment process(Timesheet timesheet) {

    BigDecimal base = timesheet.getHours().multiply(new BigDecimal("20"));
    BigDecimal overtime = timesheet.getOvertime().multiply(new BigDecimal("30"));
    BigDecimal total = base.add(overtime).subtract(timesheet.getDeductions());

    Payment payment = new Payment();
    payment.setEmployeeId(timesheet.getEmployeeId());
    payment.setAmount(total);

    log.info("Processed Timesheet[id={}, empId={}]: hours={}, overtime={}, deductions={} -> Payment: {}",
        timesheet.getId(),
        timesheet.getEmployeeId(),
        timesheet.getHours(),
        timesheet.getOvertime(),
        timesheet.getDeductions(),
        total
    );

    return payment;
  }
}
