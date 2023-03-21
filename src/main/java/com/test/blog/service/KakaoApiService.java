package com.test.blog.service;

import com.test.blog.util.WebClientUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KakaoApiService {
    private static final String KAKAO_DAUM_API_BASE_URI = "dapi.kakao.com";
    private static final String KAKAO_APP_RESTAPI_KEY = "3838bb9d56ac50f869fa6c41e874d5cb";

    public String getApiCaller(String uri) throws Exception {
        WebClient webClient = WebClientUtil.getBaseUrl(KAKAO_DAUM_API_BASE_URI);
        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + KAKAO_APP_RESTAPI_KEY)
                .retrieve()
                .toEntity(String.class)
                .blockOptional().orElseThrow(
                        () -> new Exception()
                );

        return responseEntity.getBody();
    }
}
