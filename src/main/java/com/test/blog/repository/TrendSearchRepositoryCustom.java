package com.test.blog.repository;

import com.test.blog.entity.BlogSearchEntity;

import java.util.List;

public interface TrendSearchRepositoryCustom {
    List<BlogSearchEntity> trendList(String date, String infoProvider);
}
