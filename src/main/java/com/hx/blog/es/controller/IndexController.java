package com.hx.blog.es.controller;

import com.hx.blog.es.entity.mysql.MysqlBlog;
import com.hx.blog.es.repository.MySqlBlogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    MySqlBlogRepository mySqlBlogRepository;
    
    @GetMapping("/")
    public String index() {
        List<MysqlBlog> all = mySqlBlogRepository.findAll();
        log.info("查询所有数据：all{}",all.size());
        return "index.html";
    }
}
