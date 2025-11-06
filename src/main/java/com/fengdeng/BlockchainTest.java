package com.fengdeng;

import com.fengdeng.blockchain.*;

import java.time.LocalDateTime;
import java.util.Arrays;

public class BlockchainTest {
    public static void main(String[] args) {
        Blockchain blockchain = new Blockchain();

        // 第一个农事操作：播种
        Transaction tx1 = new Transaction("APPLE-202510", "李强", "播种", "四川雅安", "播种红富士苹果", LocalDateTime.now());
        Block block1 = new Block(1, Arrays.asList(tx1), blockchain.getLatestBlock().getHash());
        blockchain.addBlock(block1);

        // 第二个操作：采摘
        Transaction tx2 = new Transaction("APPLE-202510", "李强", "采摘", "四川雅安", "果实成熟采摘", LocalDateTime.now());
        Block block2 = new Block(2, Arrays.asList(tx2), blockchain.getLatestBlock().getHash());
        blockchain.addBlock(block2);

        // 输出链信息
        System.out.println("=== 当前区块链 ===");
        for (Block b : blockchain.getChain()) {
            System.out.println("区块 #" + b.getIndex() + " | 哈希：" + b.getHash() + " | 前哈希：" + b.getPreviousHash());
        }

        // 校验完整性
        System.out.println("链是否有效？ " + blockchain.isChainValid());
    }
}
