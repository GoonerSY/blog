package com.test.blog.controller;

import com.test.blog.domain.TrendSearchReq;
import com.test.blog.domain.TrendSearchResp;
import com.test.blog.service.TrendSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class TrendController {

    private final TrendSearchService trendSearchService;

    @Autowired
    public TrendController(TrendSearchService trendSearchService) {
        this.trendSearchService = trendSearchService;
    }

    @GetMapping(value = "/trend/search")
    public TrendSearchResp trendSearch(@Valid @ModelAttribute TrendSearchReq trendSearchReq) throws Exception {
        return trendSearchService.trendSearch(trendSearchReq);
    }
}
