package com.hx.blog.es.controller;

import com.hx.blog.es.entity.es.EsBlog;
import com.hx.blog.es.entity.mysql.MysqlBlog;
import com.hx.blog.es.repository.EsBlogRepository;
import com.hx.blog.es.repository.MySqlBlogRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@Slf4j
public class DataController {
    @Autowired
    MySqlBlogRepository mySqlBlogRepository;
    
    @Autowired
    EsBlogRepository esBlogRepository;
    
    @GetMapping("/blogs")
    public Object blogList() {
        List<MysqlBlog> blogs = mySqlBlogRepository.queryAll();
        return blogs;
    }
    
    @PostMapping("/search") 
    public Object serach(@RequestBody Param param) {
        Map<String, Object> map = new HashMap<>();
        //统计耗时
        StopWatch watch = new StopWatch();
        watch.start();
        String type = param.getType();
        //mysql
        if ("mysql".equals(type)){
            List<MysqlBlog> blogs = mySqlBlogRepository.queryBlog(param.getKeyword());
            map.put("list", blogs);
        } else if ("es".equals(type)) {
            BoolQueryBuilder builder = QueryBuilders.boolQuery();
            builder.should(QueryBuilders.matchPhraseQuery("title", param.getKeyword()));
            builder.should(QueryBuilders.matchPhraseQuery("content", param.getKeyword()));
            String s = builder.toString();
            log.info("\ns={}", s);
            Page<EsBlog> search = (Page<EsBlog>) esBlogRepository.search(builder);
            List<EsBlog> content = search.getContent();
            map.put("list", content);
        }
        
        watch.stop();
        //计算耗时
        long millis = watch.getTotalTimeMillis();
        map.put("duration", millis);

        return map;
    }

    @GetMapping("/blog/{id}")
    public Object blog(@PathVariable Integer id) {
        Optional<MysqlBlog> byId = mySqlBlogRepository.findById(id);
        return byId.get();
    }
    
    @Data
    private static class Param {
        private String type;
        private String keyword;
    }
}
