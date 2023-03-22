package com.test.blog.service;

import com.test.blog.exception.BadWebClientRequestException;
import com.test.blog.util.WebClientUtil;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class NaverApiService {
    private static final String NAVER_API_BASE_URI = "https://aaaopenapi.naver.com";
    private static final String NAVER_API_CLIENT_ID = "0ubcDcCCjruRzhBo7WBJ";
    private static final String NAVER_API_CLIENT_SECRET = "iosGKoVBRS";

    public Mono<String> getApiCaller(String uri) {
        WebClient webClient = WebClientUtil.getBaseUrl(NAVER_API_BASE_URI);

        return webClient.get()
                .uri(uri)
                .header("X-Naver-Client-Id", NAVER_API_CLIENT_ID)
                .header("X-Naver-Client-Secret", NAVER_API_CLIENT_SECRET)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response ->
                        Mono.error(
                                new BadWebClientRequestException(
                                        response.rawStatusCode(),
                                        String.format("4xx 외부 요청 오류. statusCode: %s, response: %s, header: %s",
                                                response.rawStatusCode(),
                                                response.bodyToMono(String.class),
                                                response.headers().asHttpHeaders())
                                )
                        )
                )
                .onStatus(HttpStatus::is5xxServerError, response ->
                        Mono.error(
                                new WebClientResponseException(
                                        response.rawStatusCode(),
                                        String.format("5xx 외부 시스템 오류. %s", response.bodyToMono(String.class)),
                                        response.headers().asHttpHeaders(), null, null
                                )
                        )
                )
                .bodyToMono(String.class);
    }
}
