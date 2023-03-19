package com.test.blog.domain.kakao;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class Documents {
    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private Date datetime;
}
