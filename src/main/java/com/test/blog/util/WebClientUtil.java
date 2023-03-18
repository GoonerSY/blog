package com.test.blog.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

public class WebClientUtil {
    public static WebClient getBaseUrl(final String uri) {
        return WebClient.builder()
                .baseUrl(uri)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, String.valueOf(mediaType))
                .build()
                .mutate()
                .build();
    }
}
