package com.hx.blog.es;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class BlogEsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogEsApplication.class, args);
        log.info("启动成功，访问地址：http://localhost:8081/");
    }

}
