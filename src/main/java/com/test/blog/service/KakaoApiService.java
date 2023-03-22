package com.test.blog.service;

import com.test.blog.exception.BadWebClientRequestException;
import com.test.blog.util.WebClientUtil;
import io.netty.handler.timeout.ReadTimeoutException;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class KakaoApiService {
    private static final String KAKAO_DAUM_API_BASE_URI = "dapi.kakao.com";
    private static final String KAKAO_APP_RESTAPI_KEY = "3838bb9d56ac50f869fa6c41e874d5cb";

    public Mono<String> getApiCaller(String uri) {
        WebClient webClient = WebClientUtil.getBaseUrl(KAKAO_DAUM_API_BASE_URI);

        return webClient.get()
                .uri(uri)
                .header("Authorization", "KakaoAK " + KAKAO_APP_RESTAPI_KEY)
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
                .bodyToMono(String.class)
                .onErrorMap(ConnectionPoolTimeoutException.class, e -> e)
                .onErrorMap(ReadTimeoutException.class, e -> e);
    }
}
