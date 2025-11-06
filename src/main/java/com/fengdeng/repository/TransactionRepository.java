package com.fengdeng.repository;

import com.fengdeng.model.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findByProductId(String productId);
    List<TransactionEntity> findByFarmer(String farmer);
    List<TransactionEntity> findByAntiFakeCode(String antiFakeCode);
}
