package com.test.blog.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import com.test.blog.domain.kakao.Documents;
import com.test.blog.domain.kakao.Meta;
import com.test.blog.domain.naver.Items;
import com.test.blog.entity.BlogSearchEntity;
import com.test.blog.repository.BlogSearchRepository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Validated
public class BlogSearchService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final KakaoApiService kakaoApiService;
    private final NaverApiService naverApiService;
    private final BlogSearchRepository blogSearchRepository;
    @Autowired
    public BlogSearchService(KakaoApiService kakaoApiService, NaverApiService naverApiService, BlogSearchRepository blogSearchRepository) {
        this.kakaoApiService = kakaoApiService;
        this.naverApiService = naverApiService;
        this.blogSearchRepository = blogSearchRepository;
    }

    @Transactional
    public BlogSearchResp blogSearch(@Valid BlogSearchReq blogSearchReq) throws Exception {
        /* 기본값 설정 */
        String query = blogSearchReq.getKeyword();
        String sort;
        int page;
        int size;
        if(blogSearchReq.getSort() == null) sort = "accuracy"; else sort = blogSearchReq.getSort();
        if(blogSearchReq.getPage() == 0) page = 1;             else page = blogSearchReq.getPage();
        if(blogSearchReq.getSize() == 0) size = 10;            else size = blogSearchReq.getSize();

        /* API 서비스 연동 */
        BlogSearchResp blogSearchResp;
        try{
            /* 지정 블로그 검색 기능은 카카오만 제공 */
            if(blogSearchReq.getTargetBlog() != null && !blogSearchReq.getTargetBlog().trim().equals("")){
                query = blogSearchReq.getTargetBlog() + " " + blogSearchReq.getKeyword();
            }
            blogSearchResp = convertResponseData(blogSearchReq.getPage(), blogSearchReq.getSize(), "kakao",
                            kakaoApiService.getApiCaller("/v2/search/blog" + convertRequestData("kakao", query, sort, page, size)));
        } catch(Exception e) {
            blogSearchResp = convertResponseData(blogSearchReq.getPage(), blogSearchReq.getSize(), "naver",
                            naverApiService.getApiCaller("/v1/search/blog.json" + convertRequestData("naver", query, sort, page, size)));
        }

        /* 키워드 카운팅 */
        keywordCounting(blogSearchReq.getKeyword(), blogSearchResp.getMeta().getInfoProvider());

        return blogSearchResp;
    }

    /**
     * 정보제공사 요청 전문 변환
     *
     * @param infoProvider
     * @param query
     * @param sort
     * @param page
     * @param size
     * @return
     * @throws Exception
     */
    public UriComponents convertRequestData(String infoProvider, String query, String sort, int page, int size) throws Exception {

        MultiValueMap<String, String> requestParams = new LinkedMultiValueMap<String, String>();

        UriComponents uriComponents;
        if(infoProvider.equals("kakao")){
            requestParams.clear();
            requestParams.add("query", query);
            requestParams.add("sort", sort);
            requestParams.add("page", String.valueOf(page));
            requestParams.add("size", String.valueOf(size));
            uriComponents = UriComponentsBuilder.newInstance().queryParams(requestParams).build();
        } else {
            requestParams.clear();
            requestParams.add("query", query);
            if(sort.equals("accuracy")) requestParams.add("sort", "sim");
            else requestParams.add("sort", "date");
            requestParams.add("start", String.valueOf(page));
            requestParams.add("display", String.valueOf(size));
            uriComponents = UriComponentsBuilder.newInstance().queryParams(requestParams).build();
        }

        return uriComponents;
    }

    /**
     * 정보제공사 응답 전문 변환
     *
     * @param page
     * @param size
     * @param infoProvider
     * @param responseData
     * @return
     * @throws Exception
     */
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
            List<Items> itemList = gson.fromJson(responseObject.get("items").toString(), new TypeToken<List<Items>>(){}.getType());
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            for (Items item : itemList) {
                blogSearchResp.getDocuments().add(Documents.builder()
                                                            .title(item.getTitle())
                                                            .contents(item.getDescription())
                                                            .url(item.getLink())
                                                            .blogname(item.getBloggername())
                                                            .thumbnail(null)
                                                            .datetime(formatter.parse(item.getPostdate())).build());
            }
        }

        return blogSearchResp;
    }

    /**
     * 검색한 keyword를 카운팅
     * @param keyword
     */
    public void keywordCounting(String keyword, String infoProvider) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new Date(System.currentTimeMillis()));
        BlogSearchEntity blogSearchEntity = blogSearchRepository.findByKeywordAndInfoProviderAndDate(keyword, infoProvider, today).orElse(null);
        if(blogSearchEntity == null){
            blogSearchEntity = BlogSearchEntity.builder()
                    .keyword(keyword)
                    .infoProvider(infoProvider)
                    .date(today)
                    .hitCount(1L).build();
        } else {
            blogSearchEntity.setHitCount(blogSearchEntity.getHitCount() + 1);
        }
        blogSearchRepository.save(blogSearchEntity);
    }
}
