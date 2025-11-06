package com.fengdeng.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "blocks")
public class BlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int indexNumber;
    private String previousHash;
    private String hash;
    private LocalDateTime timestamp;

    // 一对多：一个区块包含多个交易
    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TransactionEntity> transactions;
}
