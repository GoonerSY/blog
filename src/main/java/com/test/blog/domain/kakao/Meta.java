package com.test.blog.domain.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Meta {
    private String infoProvider;
    private int total_count;
    private int page;
    private int size;
    @JsonProperty("is_end")
    private boolean is_end;
}
