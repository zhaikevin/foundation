package com.github.foundation.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @Description:
 * @Author: kevin
 * @Date: 2019/11/1 15:16
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.github.foundation"})
public class FoundationServer {

    public static void main(String[] args) {
        SpringApplication.run(FoundationServer.class, args);
    }
}
