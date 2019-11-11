package com.github.foundation.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/1 15:16
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.github.foundation"})
@MapperScan("com.github.foundation.test.dao")
public class FoundationServer {

    public static void main(String[] args) {
        SpringApplication.run(FoundationServer.class, args);
    }
}
