package com.fengdeng.blockchain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private String productId;   // 农产品编号
    private String farmer;      // 农户姓名
    private String action;      // 操作类型（播种/施肥/采摘/运输）
    private String location;    // 地点
    private String description; // 说明
    private LocalDateTime timestamp; // 时间戳
}
