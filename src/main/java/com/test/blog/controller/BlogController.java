package com.test.blog.controller;

import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BlogController {
    // 기본형
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @GetMapping(value = "/blog/search")
    public BlogSearchResp blogSearch(BlogSearchReq blogSearchReq){
        BlogSearchResp blogSearchResp = new BlogSearchResp();
        return blogSearchResp;
    }
}
