package com.test.blog.service;

import com.test.blog.domain.TrendSearchReq;
import com.test.blog.domain.TrendSearchResp;
import com.test.blog.entity.BlogSearchEntity;
import com.test.blog.repository.TrendSearchRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
public class TrendSearchService {

    private final TrendSearchRepositoryImpl trendSearchRepository;

    @Autowired
    public TrendSearchService(TrendSearchRepositoryImpl trendSearchRepository) {
        this.trendSearchRepository = trendSearchRepository;
    }

    public TrendSearchResp trendSearch(@Valid TrendSearchReq trendSearchReq) throws Exception {
        return TrendSearchResp.builder()
                .trendList(trendSearchRepository.trendList(trendSearchReq.getDate(), trendSearchReq.getInfoProvider()))
                .build();
    }
}
