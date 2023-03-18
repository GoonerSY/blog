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
    private static final String KAKAO_DAUM_API_BASE_URI = "https://dapi.kakao.com";

    /* DB 저장으로 변경할 것 */
    private static final String KAKAO_APP_RESTAPI_KEY = "3838bb9d56ac50f869fa6c41e874d5cb";

    public String getApiCaller(String path, String data) throws Exception {
        WebClient webClient = WebClientUtil.getBaseUrl(KAKAO_DAUM_API_BASE_URI);
        ResponseEntity<String> responseEntityMono = webClient.get()
                .uri(path + "?" + data)
                .header("Authorization", "KakaoAK" + KAKAO_APP_RESTAPI_KEY)
                .retrieve()
                .toEntity(String.class)
                .blockOptional().orElseThrow(
                        () -> new Exception()
                );

        return responseEntityMono.getBody();
    }
}
