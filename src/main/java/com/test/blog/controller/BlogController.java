package com.test.blog.controller;

import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import com.test.blog.service.BlogSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BlogController {
    // 기본형
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BlogSearchService blogSearchService;

    @GetMapping(value = "/blog/search")
    public BlogSearchResp blogSearch(@Valid @ModelAttribute BlogSearchReq blogSearchReq) throws Exception {
        logger.info("blogSearchReq : [{}]", blogSearchReq);
        return blogSearchService.blogSearch(blogSearchReq);
    }
}
