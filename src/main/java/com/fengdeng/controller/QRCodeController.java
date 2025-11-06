package com.fengdeng.controller;

import com.fengdeng.model.TransactionEntity;
import com.fengdeng.repository.TransactionRepository;
import com.fengdeng.service.BlockService;
import com.fengdeng.util.QRCodeUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/qrcode")
public class QRCodeController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private BlockService blockService;

    /**
     * 生成二维码（JSON：含 base64 和跳转链接）
     */
    @ResponseBody
    @GetMapping("/generate/{productId}")
    public Map<String, Object> generateQRCode(@PathVariable String productId) throws Exception {
        String host = InetAddress.getLocalHost().getHostAddress();
        String traceUrl = "http://" + host + ":8080/api/qrcode/trace/" + productId;

        String raw = QRCodeUtil.generateQRCodeBase64(traceUrl, 300, 300);
        String dataUrl = (raw != null && raw.startsWith("data:image"))
                ? raw
                : "data:image/png;base64," + raw;

        Map<String, Object> resp = new HashMap<>();
        resp.put("productId", productId);
        resp.put("traceUrl", traceUrl);
        resp.put("qrBase64", dataUrl);
        return resp;
    }

    /**
     * 扫码查看溯源信息页面
     */
    @GetMapping("/trace/{productId}")
    public String showTracePage(@PathVariable String productId, Model model) throws Exception {
        List<TransactionEntity> transactions = transactionRepository.findByProductId(productId);

        model.addAttribute("productId", productId);

        String host = InetAddress.getLocalHost().getHostAddress();
        String traceUrl = "http://" + host + ":8080/api/qrcode/trace/" + productId;
        model.addAttribute("traceUrl", traceUrl);

        if (transactions == null || transactions.isEmpty()) {
            model.addAttribute("transactions", List.of());
            model.addAttribute("error", "未找到该产品的上链记录");
            return "trace";
        }

        model.addAttribute("transactions", transactions);
        return "trace";
    }

    /**
     * 显示农户录入页面
     */
    @GetMapping("/add")
    public String showAddForm() {
        return "add";
    }

    /**
     * 历史上链记录页面
     */
    @GetMapping("/history")
    public String showHistoryPage(HttpSession session, Model model) {
        Object userObj = session.getAttribute("user");
        if (userObj == null) {
            return "redirect:/login";
        }
        String farmer = ((com.fengdeng.model.UserEntity) userObj).getUsername();
        List<TransactionEntity> transactions = transactionRepository.findByFarmer(farmer);
        model.addAttribute("transactions", transactions);
        model.addAttribute("farmer", farmer);
        return "history";
    }
}
