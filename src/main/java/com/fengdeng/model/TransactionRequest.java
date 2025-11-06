package com.fengdeng.model;

import lombok.Data;

@Data
public class TransactionRequest {
    private String productId;
    private String farmer;
    private String action;
    private String location;
    private String description;
}
