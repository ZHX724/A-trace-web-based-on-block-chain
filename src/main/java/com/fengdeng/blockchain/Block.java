package com.fengdeng.blockchain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;

public class Block {
    private int index;
    private LocalDateTime timestamp;
    private List<Transaction> transactions;
    private String previousHash;
    private String hash;

    public Block(int index, List<Transaction> transactions, String previousHash) {
        this.index = index;
        this.transactions = transactions;
        this.previousHash = previousHash;
        this.timestamp = LocalDateTime.now();
        this.hash = calculateHash();
    }

    // ✅ 修复 LocalDateTime 序列化问题
    public String calculateHash() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules(); // 支持 Java 8 时间类型
        try {
            String blockData = index + timestamp.toString() + previousHash + mapper.writeValueAsString(transactions);
            return HashUtil.applySha256(blockData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public int getIndex() { return index; }
    public String getHash() { return hash; }
    public String getPreviousHash() { return previousHash; }
    public List<Transaction> getTransactions() { return transactions; }
}
