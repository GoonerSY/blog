package com.test.blog.service;

import com.google.gson.Gson;
import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import com.test.blog.domain.kakao.Documents;
import com.test.blog.domain.kakao.Meta;
import com.test.blog.entity.BlogSearchEntity;
import com.test.blog.repository.BlogSearchRepository;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.SimpleDateFormat;

@Service
@Validated
public class BlogSearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private KakaoApiService kakaoApiService;
    @Autowired
    private NaverApiService naverApiService;
    @Autowired
    private BlogSearchRepository blogSearchRepository;

    public BlogSearchResp blogSearch(@Valid BlogSearchReq blogSearchReq) throws Exception {
        /* 기본값 설정 */
        if(blogSearchReq.getSort() == null) blogSearchReq.setSort("accuracy");
        if(blogSearchReq.getPage() == 0) blogSearchReq.setPage(1);
        if(blogSearchReq.getSize() == 0) blogSearchReq.setSize(10);

        /* 키워드 카운팅 */
        keywordCounting(blogSearchReq.getKeyword());

        /* 요청값 쿼리스트링 변환처리 */
        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<String, String>();
        UriComponents uriComponents;
        /* 카카오 API 서비스 연동 */
        BlogSearchResp blogSearchResp;
        try{
            String query = blogSearchReq.getKeyword();
            if(blogSearchReq.getTargetBlog() != null && !blogSearchReq.getTargetBlog().trim().equals("")){
                query = blogSearchReq.getTargetBlog() + " " + blogSearchReq.getKeyword();
            }
            requestParams.clear();
            requestParams.add("query", query);
            requestParams.add("sort", blogSearchReq.getSort());
            requestParams.add("page", String.valueOf(blogSearchReq.getPage()));
            requestParams.add("size", String.valueOf(blogSearchReq.getSize()));
            uriComponents = UriComponentsBuilder.newInstance().queryParams(requestParams).build();

            blogSearchResp = convertResponseData(blogSearchReq.getPage(), blogSearchReq.getSize(), "kakao", kakaoApiService.getApiCaller("/v2/search/blog" + uriComponents.toUri()));
        } catch(Exception e) {
            requestParams.clear();
            requestParams.add("query", blogSearchReq.getKeyword());
            if(blogSearchReq.getSort().equals("accuracy")) requestParams.add("sort", "sim");
            else requestParams.add("sort", "date");
            requestParams.add("start", String.valueOf(blogSearchReq.getPage()));
            requestParams.add("display", String.valueOf(blogSearchReq.getSize()));
            uriComponents = UriComponentsBuilder.newInstance().queryParams(requestParams).build();

            blogSearchResp = convertResponseData(blogSearchReq.getPage(), blogSearchReq.getSize(), "naver", naverApiService.getApiCaller("/v1/search/blog.json" + uriComponents.toUri()));
        }

        return blogSearchResp;
    }

    @Transactional
    public void keywordCounting(String keyword) {
        /* 검색 키워드 카운팅 */
        BlogSearchEntity blogSearchEntity = blogSearchRepository.findByKeyword(keyword);
        if(blogSearchEntity == null){
            blogSearchEntity = new BlogSearchEntity();
            blogSearchEntity.setKeyword(keyword);
            blogSearchEntity.setHitCount(1L);
        } else {
            blogSearchEntity.setHitCount(blogSearchEntity.getHitCount() + 1);
        }
        blogSearchRepository.saveAndFlush(blogSearchEntity);
    }

    public BlogSearchResp convertResponseData(int page, int size, String infoProvider, String responseData) throws Exception {
        Gson gson = new Gson();
        logger.info("responseData : [{}]", responseData);
        BlogSearchResp blogSearchResp = new BlogSearchResp();
        if(infoProvider.equals("kakao")){
            blogSearchResp = gson.fromJson(responseData, BlogSearchResp.class);
            blogSearchResp.getMeta().setInfoProvider(infoProvider);
            blogSearchResp.getMeta().setPage(page);
            blogSearchResp.getMeta().setSize(size);
        } else {
            JSONParser parser = new JSONParser();
            JSONObject responseObject = (JSONObject) parser.parse(responseData);

            /* Meta 객체 구성 */
            int responsePage = Integer.parseInt(responseObject.get("start").toString());
            int responseSize = Integer.parseInt(responseObject.get("display").toString());
            int responseTotal = Integer.parseInt(responseObject.get("total").toString());

            boolean is_end = false;
            if(responsePage * responseSize >= responseTotal){
                is_end = true;
            }

            blogSearchResp.setMeta(Meta.builder()
                                    .infoProvider(infoProvider)
                                    .total_count(responseTotal)
                                    .page(responsePage)
                                    .size(responseSize)
                                    .is_end(is_end).build());

            /* Documents 객체 구성 */
            JSONArray items = (JSONArray)responseObject.get("items");
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            for (JSONObject item : (Iterable<JSONObject>) items) {
                blogSearchResp.getDocuments().add(Documents.builder()
                                                            .title(item.get("title").toString())
                                                            .contents(item.get("description").toString())
                                                            .url(item.get("link").toString())
                                                            .blogname(item.get("bloggername").toString())
                                                            .thumbnail(null)
                                                            .datetime(formatter.parse(item.get("postdate").toString())).build());
            }
        }

        return blogSearchResp;
    }
}
