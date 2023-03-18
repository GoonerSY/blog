package com.test.blog.service;

import com.test.blog.util.WebClientUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.Map.Entry;

@Service
public class BlogService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final String KAKAO_DAUM_API_BASE_URI = "dapi.kakao.com";

    /* DB 저장으로 변경할 것 */
    private static final String KAKAO_APP_RESTAPI_KEY = "3838bb9d56ac50f869fa6c41e874d5cb";

    @Autowired
    private KakaoApiService kakaoApiService;

    public String blogSearch(Map<String, Object> parameters) {
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<String, String>();
        for (Entry<String, Object> entry : parameters.entrySet()) {
            requestParams.add(entry.getKey(), entry.getValue().toString());
        }

        UriComponents uriComponents = UriComponentsBuilder.newInstance()
                .queryParams(requestParams).build();

        WebClient webClient = WebClientUtil.getBaseUrl(KAKAO_DAUM_API_BASE_URI);
        ResponseEntity<String> responseEntityMono = webClient.get()
                .uri("/v2/search/blog" + uriComponents.toUri())
                .header("Authorization", "KakaoAK " + KAKAO_APP_RESTAPI_KEY)
                .retrieve()
                .toEntity(String.class)
                .block();

        return responseEntityMono.getBody();
    }
}
