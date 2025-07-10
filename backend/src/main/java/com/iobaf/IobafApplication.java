package com.iobaf;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * IOBAF系统启动类
 * 
 * @author IOBAF Team
 * @since 2024-01-01
 */
@SpringBootApplication
@MapperScan("com.iobaf.repository")
public class IobafApplication {

    public static void main(String[] args) {
        SpringApplication.run(IobafApplication.class, args);
        System.out.println("IOBAF系统启动成功！");
    }
} 