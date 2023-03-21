package com.test.blog.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class BlogSearchEntity {
    @Id
    private String keyword;
    @Column
    private String infoProvider;
    private String date;
    @Column
    private Long hitCount;

    @Builder
    public BlogSearchEntity(String keyword, String infoProvider, String date, Long hitCount){
        this.keyword = keyword;
        this.infoProvider = infoProvider;
        this.date = date;
        this.hitCount = hitCount;
    }
}
