package com.test.blog.repository;

import com.test.blog.entity.BlogSearchEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.LockModeType;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BlogSearchRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BlogSearchRepository blogSearchRepository;

    @Test
    public void testFindByKeyword() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(2);
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // when
        entityManager.clear();
        BlogSearchEntity result1 = blogSearchRepository.findByKeyword("test");
        BlogSearchEntity result2 = blogSearchRepository.findByKeyword("test");

        executorService.submit(() -> {
            entityManager.clear();
            BlogSearchEntity entity1 = entityManager.find(BlogSearchEntity.class, result1.getKeyword());
            entity1.setHitCount(entity1.getHitCount());
            blogSearchRepository.save(entity1);
            latch.countDown();
        });

        executorService.submit(() -> {
            entityManager.clear();
            BlogSearchEntity entity2 = entityManager.find(BlogSearchEntity.class, result2.getKeyword());
            entity2.setHitCount(entity2.getHitCount());
            blogSearchRepository.save(entity2);
            latch.countDown();
        });

        latch.await(1, TimeUnit.SECONDS);

        // then
        Assertions.assertThrows(OptimisticLockingFailureException.class, () -> entityManager.flush());
//        entityManager.flush();
        BlogSearchEntity result = blogSearchRepository.findByKeyword("test");
        Assertions.assertEquals(2, result.getHitCount());
    }
}
