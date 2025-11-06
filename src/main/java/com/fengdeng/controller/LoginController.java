package com.fengdeng.controller;

import com.fengdeng.model.UserEntity;
import com.fengdeng.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 显示登录页面
     */
    @GetMapping
    public String showLoginPage() {
        System.out.println("✅ 加载登录页面");
        return "login";
    }

    /**
     * 处理登录提交
     */
    @PostMapping
    public String handleLogin(@RequestParam String username,
                              @RequestParam String password,
                              HttpSession session,
                              Model model) {

        System.out.println("➡️ 登录请求: " + username);

        UserEntity user = userRepository.findByUsername(username);

        if (user == null || !user.getPassword().equals(password)) {
            model.addAttribute("error", "❌ 用户名或密码错误");
            return "login";
        }

        // 保存登录状态
        session.setAttribute("user", user);
        System.out.println("✅ 登录成功：" + username);

        // 登录后跳转录入页面
        return "redirect:/api/qrcode/add";
    }

    /**
     * 退出登录
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
