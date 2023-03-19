package com.test.blog.service;

import com.test.blog.util.WebClientUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class NaverApiService {
    private static final String NAVER_API_BASE_URI = "https://openapi.naver.com";

    /* DB 저장으로 변경할 것 */
    private static final String NAVER_API_CLIENT_ID = "0ubcDcCCjruRzhBo7WBJ";
    private static final String NAVER_API_CLIENT_SECRET = "iosGKoVBRS";
    public String getApiCaller(String uri) throws Exception {
        WebClient webClient = WebClientUtil.getBaseUrl(NAVER_API_BASE_URI);
        ResponseEntity<String> responseEntity = webClient.get()
                .uri(uri)
                .header("X-Naver-Client-Id", NAVER_API_CLIENT_ID)
                .header("X-Naver-Client-Secret", NAVER_API_CLIENT_SECRET)
                .retrieve()
                .toEntity(String.class)
                .blockOptional().orElseThrow(
                        () -> new Exception()
                );

        return responseEntity.getBody();
    }
}
