package com.test.blog.controller;

import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import com.test.blog.service.BlogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class BlogController {
    // 기본형
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlogService blogService;

    @GetMapping(value = "/blog/search")
    public String blogSearch(@RequestParam Map<String, Object> allParameters){
        return blogService.blogSearch(allParameters);
    }
}
