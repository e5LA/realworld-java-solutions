package com.realworldjava.batchprocessing.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.realworldjava.batchprocessing.entity.Payment;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class PaymentRepositoryTest {

  @Autowired
  private PaymentRepository repository;

  @Test
  void saveAllAndFindAll_shouldWorkCorrectly() {
    // given
    var payment1 = new Payment();
    payment1.setEmployeeId(1);
    payment1.setAmount(new BigDecimal("1500.00"));

    Payment payment2 = new Payment();
    payment2.setEmployeeId(2);
    payment2.setAmount(new BigDecimal("1800.00"));

    repository.saveAll(List.of(payment1, payment2));

    // when
    List<Payment> results = repository.findAll();

    // then
    assertThat(results).hasSize(2);
    assertThat(results)
        .extracting(Payment::getEmployeeId)
        .containsExactlyInAnyOrder(1, 2);
  }
}
