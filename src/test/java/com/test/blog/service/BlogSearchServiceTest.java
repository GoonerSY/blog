package com.test.blog.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.test.blog.entity.BlogSearchEntity;
import com.test.blog.repository.BlogSearchRepository;
import com.test.blog.repository.BlogSearchRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Component
class TestConfig {
    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }
}

@DataJpaTest
@Import(TestConfig.class)
public class BlogSearchServiceTest {
    private final BlogSearchRepository blogSearchRepository;
    private final BlogSearchRepositoryImpl blogSearchRepositoryImpl;
    @Autowired
    public BlogSearchServiceTest(BlogSearchRepository blogSearchRepository, BlogSearchRepositoryImpl blogSearchRepositoryImpl) {
        this.blogSearchRepository = blogSearchRepository;
        this.blogSearchRepositoryImpl = blogSearchRepositoryImpl;
    }

    @Test
    public void keyword_counting_test() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date(System.currentTimeMillis()));
        BlogSearchEntity blogSearchEntity1 = blogSearchRepository.findByKeywordAndInfoProviderAndDate("test", "kakao", today).orElse(null);
        if(blogSearchEntity1 == null){
            blogSearchEntity1 = BlogSearchEntity.builder()
                    .keyword("test")
                    .infoProvider("kakao")
                    .date(today)
                    .hitCount(1L).build();
        } else {
            blogSearchEntity1.setHitCount(blogSearchEntity1.getHitCount() + 1);
        }
        blogSearchRepository.saveAndFlush(blogSearchEntity1);

        BlogSearchEntity blogSearchEntity2 = blogSearchRepository.findByKeywordAndInfoProviderAndDate("test", "naver", today).orElse(null);
        if(blogSearchEntity2 == null){
            blogSearchEntity2 = BlogSearchEntity.builder()
                    .keyword("test")
                    .infoProvider("naver")
                    .date(today)
                    .hitCount(1L).build();
        } else {
            blogSearchEntity2.setHitCount(blogSearchEntity2.getHitCount() + 1);
        }
        blogSearchRepository.saveAndFlush(blogSearchEntity2);

        assertThat(blogSearchRepositoryImpl.trendList(today, "").size(), equalTo(2));
    }
}
