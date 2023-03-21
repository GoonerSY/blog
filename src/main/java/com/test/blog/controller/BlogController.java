package com.test.blog.controller;

import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import com.test.blog.service.BlogSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class BlogController {
    private final BlogSearchService blogSearchService;

    @Autowired
    public BlogController(BlogSearchService blogSearchService) {
        this.blogSearchService = blogSearchService;
    }

    @GetMapping(value = "/blog/search")
    public BlogSearchResp blogSearch(@Valid @ModelAttribute BlogSearchReq blogSearchReq) throws Exception {
        return blogSearchService.blogSearch(blogSearchReq);
    }
}
