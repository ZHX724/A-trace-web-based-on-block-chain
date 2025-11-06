package com.fengdeng.service;

import com.fengdeng.blockchain.Block;
import com.fengdeng.blockchain.Blockchain;
import com.fengdeng.blockchain.Transaction;
import com.fengdeng.model.BlockEntity;
import com.fengdeng.model.TransactionEntity;
import com.fengdeng.repository.BlockRepository;
import com.fengdeng.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 区块链服务层
 * 负责：区块生成、交易上链、数据库同步、防伪码生成与复用
 */
@Service
public class BlockService {

    private final Blockchain blockchain;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    public BlockService() {
        this.blockchain = new Blockchain();
    }

    /**
     * 添加交易并上链 + 存入数据库
     */
    public Block addTransaction(String productId, String farmer, String action,
                                String location, String description) {

        // 1️⃣ 创建交易对象（内存模型）
        Transaction tx = new Transaction(productId, farmer, action, location, description, LocalDateTime.now());

        // 2️⃣ 生成新区块（挂接到区块链）
        Block newBlock = new Block(blockchain.getChain().size(),
                Arrays.asList(tx),
                blockchain.getLatestBlock().getHash());
        blockchain.addBlock(newBlock);

        // 3️⃣ 转换为数据库实体类
        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setIndexNumber(newBlock.getIndex());
        blockEntity.setPreviousHash(newBlock.getPreviousHash());
        blockEntity.setHash(newBlock.getHash());
        blockEntity.setTimestamp(LocalDateTime.now());

        // 4️⃣ 处理交易记录并添加防伪码
        List<TransactionEntity> txEntities = new ArrayList<>();

        for (Transaction t : newBlock.getTransactions()) {
            TransactionEntity txEntity = new TransactionEntity();
            txEntity.setProductId(t.getProductId());
            txEntity.setFarmer(t.getFarmer());
            txEntity.setAction(t.getAction());
            txEntity.setLocation(t.getLocation());
            txEntity.setDescription(t.getDescription());
            txEntity.setTimestamp(t.getTimestamp());
            txEntity.setBlock(blockEntity);

            // ✅ 检查数据库是否已有该产品的防伪码（同产品共用）
            String antiFakeCode;
            List<TransactionEntity> existing = transactionRepository.findByProductId(t.getProductId());
            if (!existing.isEmpty()) {
                antiFakeCode = existing.get(0).getAntiFakeCode(); // 已存在则复用
            } else {
                antiFakeCode = generateAntiFakeCode(); // 生成新的防伪码
            }
            txEntity.setAntiFakeCode(antiFakeCode);

            txEntities.add(txEntity);
        }

        // 5️⃣ 持久化存储区块与交易
        blockEntity.setTransactions(txEntities);
        blockRepository.save(blockEntity);

        // ✅ 打印日志
        System.out.println("✅ 产品 " + productId + " 已上链，防伪码：" + txEntities.get(0).getAntiFakeCode());

        return newBlock;
    }

    /**
     * 生成 18 位防伪码（随机数字 + 大小写字母）
     */
    private String generateAntiFakeCode() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 18; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    /**
     * 获取当前区块链
     */
    public List<Block> getBlockchain() {
        return blockchain.getChain();
    }

    /**
     * 校验区块链完整性
     */
    public boolean isChainValid() {
        return blockchain.isChainValid();
    }
}
