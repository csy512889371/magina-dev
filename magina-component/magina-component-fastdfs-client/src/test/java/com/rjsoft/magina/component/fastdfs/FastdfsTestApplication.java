package com.rjsoft.magina.component.fastdfs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.support.RegistrationPolicy;

/**
 * 测试驱动类
 * 
 *
 *
 */
@SpringBootApplication
@EnableMBeanExport(registration = RegistrationPolicy.IGNORE_EXISTING)
public class FastdfsTestApplication {
    public static void main(String[] args) {
        SpringApplication.run(FastdfsTestApplication.class, args);
    }

}
