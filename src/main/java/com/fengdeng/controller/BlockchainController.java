package com.fengdeng.controller;

import com.fengdeng.blockchain.Block;
import com.fengdeng.model.TransactionEntity;
import com.fengdeng.repository.TransactionRepository;
import com.fengdeng.service.BlockService;
import com.fengdeng.util.QRCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/trace")
public class BlockchainController {

    @Autowired
    private BlockService blockService;

    @Autowired
    private TransactionRepository transactionRepository;

    /**
     * 表单上链：返回 JSON（含防伪码、二维码、跳转链接）
     */
    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> addTrace(
            @RequestParam("productId") String productId,
            @RequestParam("farmer") String farmer,
            @RequestParam("action") String action,
            @RequestParam("location") String location,
            @RequestParam("description") String description) {

        Map<String, Object> resp = new HashMap<>();
        try {
            // 1) 业务上链（会持久化 TransactionEntity）
            blockService.addTransaction(productId, farmer, action, location, description);

            // 2) 取该产品最新一条交易，拿防伪码
            List<TransactionEntity> txList = transactionRepository.findByProductId(productId);
            if (txList == null || txList.isEmpty()) {
                resp.put("status", "fail");
                resp.put("message", "上链完成但未在数据库中找到交易记录");
                return ResponseEntity.status(500).body(resp);
            }
            TransactionEntity tx = txList.get(txList.size() - 1);

            // 3) 生成追溯链接 & 二维码
            String host = InetAddress.getLocalHost().getHostAddress();
            String traceUrl = "http://" + host + ":8080/api/qrcode/trace/" + productId;

            String rawBase64 = QRCodeUtil.generateQRCodeBase64(traceUrl, 300, 300);
            String dataUrl = (rawBase64 != null && rawBase64.startsWith("data:image"))
                    ? rawBase64
                    : "data:image/png;base64," + rawBase64;

            // 4) 组装返回
            resp.put("status", "success");
            resp.put("productId", productId);
            resp.put("antiFakeCode", tx.getAntiFakeCode());
            resp.put("traceUrl", traceUrl);
            resp.put("qrBase64", dataUrl);

            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.put("status", "fail");
            resp.put("message", e.getClass().getSimpleName() + ": " + e.getMessage());
            return ResponseEntity.status(500).body(resp);
        }
    }

    @GetMapping("/all")
    public List<Block> getAllBlocks() {
        return blockService.getBlockchain();
    }

    @GetMapping("/check")
    public String checkChain() {
        boolean valid = blockService.isChainValid();
        return valid ? "✅ 区块链有效，未被篡改！" : "⚠️ 区块链异常，检测到篡改！";
    }
}

