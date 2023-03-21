package com.test.blog.service;

import com.test.blog.domain.TrendSearchReq;
import com.test.blog.domain.TrendSearchResp;
import com.test.blog.repository.BlogSearchRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;

@Service
public class TrendSearchService {

    private final BlogSearchRepositoryImpl blogSearchRepositoryImpl;

    @Autowired
    public TrendSearchService(BlogSearchRepositoryImpl blogSearchRepositoryImpl) {
        this.blogSearchRepositoryImpl = blogSearchRepositoryImpl;
    }

    public TrendSearchResp trendSearch(@Valid TrendSearchReq trendSearchReq) throws Exception {
        return TrendSearchResp.builder()
                .trendList(blogSearchRepositoryImpl.trendList(trendSearchReq.getDate(), trendSearchReq.getInfoProvider()))
                .build();
    }
}
