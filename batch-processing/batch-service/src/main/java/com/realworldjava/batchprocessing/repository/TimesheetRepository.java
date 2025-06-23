package com.realworldjava.batchprocessing.repository;

import com.realworldjava.batchprocessing.entity.Timesheet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {

  Slice<Timesheet> findByApprovedIsTrueAndProcessedIsFalse(Pageable pageable);

}
