package com.fengdeng.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "transactions")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 产品唯一编号（如 APPLE-20251019）
    @Column(nullable = false)
    private String productId;

    // 农户名
    @Column(nullable = false)
    private String farmer;

    // 农事操作（采摘、施肥、运输等）
    @Column(nullable = false)
    private String action;

    // 操作地点
    private String location;

    // 操作详细描述
    @Column(length = 500)
    private String description;

    // 上链时间
    private LocalDateTime timestamp;

    // ✅ 新增：防伪码字段（18位随机码）
    @Column(length = 32)
    private String antiFakeCode;

    // 区块外键（多条交易属于同一个区块）
    @ManyToOne
    @JoinColumn(name = "block_id")
    private BlockEntity block;
}
