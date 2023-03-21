package com.test.blog.repository;

import com.test.blog.entity.BlogSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface BlogSearchRepositoryCustom {
    List<BlogSearchEntity> trendList(String date, String infoProvider);
}
