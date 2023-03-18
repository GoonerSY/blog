package com.test.blog.repository;

import com.test.blog.entity.BlogSearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;

@Repository
public interface BlogSearchRepository extends JpaRepository<BlogSearchEntity, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    BlogSearchEntity findByKeyword(String keyword);
}
