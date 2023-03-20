package com.test.blog.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@Entity
@NoArgsConstructor
public class BlogSearchEntity {
    @Id
    private String keyword;
    @Column
    private Long hitCount;

    @Builder
    public BlogSearchEntity(String keyword, Long hitCount){
        this.keyword = keyword;
        this.hitCount = hitCount;
    }
}
