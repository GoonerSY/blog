package com.test.blog.repository;

import com.test.blog.entity.BlogSearchEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Component;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Component
class AsyncTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void run(Runnable runnable) {
        runnable.run();
    }
}

@Import(AsyncTransaction.class)
@DataJpaTest
@TestPropertySource(
        properties = {
                "spring.sql.init.mode=embedded",
                "spring.sql.init.schema-locations=classpath:schema.sql",
                "spring.sql.init.data-locations=classpath:data.sql",
                "spring.jpa.defer-datasource-initialization=true"
        }
)
public class BlogSearchRepositoryTest {

    private final AsyncTransaction asyncTransaction;
    private final BlogSearchRepository blogSearchRepository;
    @Autowired
    public BlogSearchRepositoryTest(AsyncTransaction asyncTransaction, BlogSearchRepository blogSearchRepository) {
        this.asyncTransaction = asyncTransaction;
        this.blogSearchRepository = blogSearchRepository;
    }

    void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void optimistic_lock_with_repository() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date(System.currentTimeMillis()));
        CompletableFuture<Void> tx = CompletableFuture.runAsync(() -> asyncTransaction.run(() -> {
            BlogSearchEntity blogSearchEntity = blogSearchRepository.findByKeywordAndInfoProviderAndDate("test", "kakao", today).orElseThrow(NullPointerException::new);
            blogSearchEntity.setHitCount(blogSearchEntity.getHitCount() + 1);
            sleep(1000);
        }));
        CompletableFuture.runAsync(() -> asyncTransaction.run(() -> {
            BlogSearchEntity blogSearchEntity = blogSearchRepository.findByKeywordAndInfoProviderAndDate("test", "kakao", today).orElseThrow(NullPointerException::new);
            blogSearchEntity.setHitCount(blogSearchEntity.getHitCount() + 1);
        })).join();
        tx.join();

        BlogSearchEntity blogSearchEntity = blogSearchRepository.findByKeywordAndInfoProviderAndDate("test", "kakao", today).orElseThrow(NullPointerException::new);
        assertThat(blogSearchEntity.getHitCount(), equalTo(3L));
    }
}