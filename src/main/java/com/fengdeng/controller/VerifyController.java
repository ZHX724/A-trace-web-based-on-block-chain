package com.fengdeng.controller;

import com.fengdeng.model.TransactionEntity;
import com.fengdeng.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/verify")
public class VerifyController {

    @Autowired
    private TransactionRepository transactionRepository;

    //显示防伪验证页面
    @GetMapping
    public String showVerifyPage() {
        return "verify";
    }

    //验证防伪码逻辑
    @PostMapping
    public String verifyCode(@RequestParam String code, Model model) {
        List<TransactionEntity> txs = transactionRepository.findByAntiFakeCode(code);

        if (txs.isEmpty()) {
            model.addAttribute("error", " 未找到该防伪码或产品未上链！");
            return "verify";
        }

        // 匹配成功 → 跳转到溯源页面
        String productId = txs.get(0).getProductId();
        return "redirect:/api/qrcode/trace/" + productId;
    }
}
