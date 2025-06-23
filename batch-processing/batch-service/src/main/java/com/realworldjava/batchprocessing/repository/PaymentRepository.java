package com.realworldjava.batchprocessing.repository;

import com.realworldjava.batchprocessing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> { }
