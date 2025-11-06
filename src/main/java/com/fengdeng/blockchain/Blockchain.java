package com.fengdeng.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {

    private List<Block> chain;

    public Blockchain() {
        chain = new ArrayList<>();
        chain.add(createGenesisBlock());
    }

    // 创建创世区块（链的第一个区块）
    private Block createGenesisBlock() {
        return new Block(0, new ArrayList<>(), "0");
    }

    // 获取最新区块
    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    // 添加新区块
    public void addBlock(Block newBlock) {
        newBlock = new Block(chain.size(), newBlock.getTransactions(), getLatestBlock().getHash());
        chain.add(newBlock);
    }

    // 校验整条链是否被篡改
    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.getHash().equals(current.calculateHash())) {
                System.out.println("区块 " + i + " 的哈希不匹配！");
                return false;
            }

            if (!current.getPreviousHash().equals(previous.getHash())) {
                System.out.println("区块 " + i + " 的前哈希不匹配！");
                return false;
            }
        }
        return true;
    }

    public List<Block> getChain() {
        return chain;
    }
}
