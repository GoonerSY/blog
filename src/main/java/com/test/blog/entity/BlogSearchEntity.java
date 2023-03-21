package com.test.blog.entity;

import lombok.*;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(indexes = @Index(name="blog_search_entity_unique_idx", columnList = "keyword, infoProvider, date", unique = true))
public class BlogSearchEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long sequence;
    @Column
    private String keyword;
    @Column
    private String infoProvider;
    @Column
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
