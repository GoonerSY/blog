package com.test.blog.domain;

import com.test.blog.entity.BlogSearchEntity;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TrendSearchResp {
    private List<BlogSearchEntity> trendList = new ArrayList<>();

    @Builder
    public TrendSearchResp(List<BlogSearchEntity> trendList) {
        this.trendList = trendList;
    }
}
