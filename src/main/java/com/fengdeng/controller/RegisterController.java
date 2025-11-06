package com.fengdeng.controller;

import com.fengdeng.model.UserEntity;
import com.fengdeng.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 显示注册页面
     */
    @GetMapping
    public String showRegisterPage() {
        System.out.println(" 加载注册页面");
        return "register";
    }

    /**
     * 处理注册提交
     */
    @Transactional
    @PostMapping
    public String handleRegister(@RequestParam String username,
                                 @RequestParam String password,
                                 Model model) {

        System.out.println(" 进入注册接口, username=" + username);

        // 检查是否已存在
        UserEntity existing = userRepository.findByUsername(username);
        if (existing != null) {
            model.addAttribute("error", " 用户名已存在，请更换！");
            return "register";
        }

        // 创建并保存用户
        UserEntity newUser = new UserEntity();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setRole("FARMER");
        userRepository.save(newUser);

        System.out.println(" 注册成功: " + username);

        model.addAttribute("success", "注册成功，请登录！");
        return "login";
    }
}
