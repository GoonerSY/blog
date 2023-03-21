package com.test.blog.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.blog.entity.BlogSearchEntity;
import com.test.blog.entity.QBlogSearchEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BlogSearchRepositoryImpl implements BlogSearchRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QBlogSearchEntity blogSearchEntity = QBlogSearchEntity.blogSearchEntity;

    public List<BlogSearchEntity> trendList(String date, String infoProvider) {
        return jpaQueryFactory.selectFrom(blogSearchEntity)
                .where(dateEq(date), infoProviderEq(infoProvider))
                .orderBy(blogSearchEntity.hitCount.desc())
                .fetch();
    }

    private BooleanExpression dateEq(String date) {
        return date != null && date != ""? blogSearchEntity.date.eq(date) : null;
    }

    private BooleanExpression infoProviderEq(String infoProvider) {
        return infoProvider != null && infoProvider != ""? blogSearchEntity.infoProvider.eq(infoProvider) : null;
    }
}
