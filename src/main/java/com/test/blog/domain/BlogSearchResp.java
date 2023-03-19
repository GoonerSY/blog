package com.test.blog.domain;

import com.test.blog.domain.kakao.Documents;
import com.test.blog.domain.kakao.Meta;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BlogSearchResp {
    private Meta meta;
    private List<Documents> documents = new ArrayList<>();
}
