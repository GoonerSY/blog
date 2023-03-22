package com.test.blog.repository;

import com.test.blog.entity.BlogSearchEntity;

import java.util.List;

public interface BlogSearchRepositoryCustom {
    List<BlogSearchEntity> trendList(String date, String infoProvider);
}
