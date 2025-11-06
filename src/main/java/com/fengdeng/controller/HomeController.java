package com.fengdeng.controller;

import com.fengdeng.repository.TransactionRepository;
import com.fengdeng.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public HomeController(UserRepository userRepository,
                          TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping({"/", "/index"}) // ← 同时映射两条路径
    public String index(Model model) {
        long farmerCount;
        try {
            farmerCount = userRepository.countByRole("FARMER");
        } catch (Exception e) {
            farmerCount = userRepository.count(); // 兜底：没有该方法就用总用户数
        }

        long totalTx = transactionRepository.count();
        model.addAttribute("farmerCount", farmerCount);
        model.addAttribute("totalTx", totalTx);
        model.addAttribute("now", LocalDateTime.now());

        return "index";
    }
}
