package com.realworldjava.batchprocessing.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.realworldjava.batchprocessing.entity.Timesheet;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

@DataJpaTest
class TimesheetRepositoryTest {

  @Autowired
  private TimesheetRepository repository;

  @Test
  void findByApprovedIsTrueAndProcessedIsFalse_shouldFilterCorrectly() {
    // given
    var timesheet1 = new Timesheet();
    timesheet1.setEmployeeId(1);
    timesheet1.setHours(new BigDecimal("160"));
    timesheet1.setApproved(true);
    timesheet1.setProcessed(true);

    var timesheet2 = new Timesheet();
    timesheet2.setEmployeeId(2);
    timesheet2.setHours(new BigDecimal("160"));
    timesheet2.setApproved(false); // not approved
    timesheet2.setProcessed(false);

    var timesheet3 = new Timesheet();
    timesheet3.setEmployeeId(3);
    timesheet3.setHours(new BigDecimal("160"));
    timesheet3.setApproved(true);
    timesheet3.setProcessed(false); // already processed

    repository.saveAll(List.of(timesheet1, timesheet2, timesheet3));

    // when
    Slice<Timesheet> results = repository.findByApprovedIsTrueAndProcessedIsFalse(
        PageRequest.of(0, 10));

    // then
    assertThat(results).hasSize(1);
    assertThat(results.getContent().get(0).getEmployeeId()).isEqualTo(3L);
  }
}