package com.test.blog.domain.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meta {
    private String infoProvider;
    private int total_count;
    private int page;
    private int size;
    @JsonProperty("is_end")
    private boolean is_end;

    @Builder
    public Meta(String infoProvider, int total_count, int page, int size, boolean is_end) {
        this.infoProvider = infoProvider;
        this.total_count = total_count;
        this.page = page;
        this.size = size;
        this.is_end = is_end;
    }
}
