package com.fengdeng;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FengDengApplication {

    public static void main(String[] args) {
        SpringApplication.run(FengDengApplication.class, args);
        System.out.println("丰登系统启动成功！Spring Boot + Web3j + ZXing 环境已就绪。");
    }
}
