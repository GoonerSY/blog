package com.test.blog.domain;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class BlogSearchReq {
    @NotBlank
    private String keyword;
    private String targetBlog;
    @Pattern(regexp = "accuracy|recency", message = "accuracy와 recency 중 하나여야 합니다.")
    private String sort;
    private int page;
    private int size;
}
