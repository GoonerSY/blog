package com.test.blog.domain;

import lombok.Data;

@Data
public class BlogSearchReq {
    private String query;
    private String sort;
    private int page;
    private int size;
}
