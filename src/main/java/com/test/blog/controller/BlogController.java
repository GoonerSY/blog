package com.test.blog.controller;

import com.test.blog.domain.BlogSearchReq;
import com.test.blog.domain.BlogSearchResp;
import com.test.blog.domain.TransactionResult;
import com.test.blog.enumeration.TransactionCode;
import com.test.blog.service.BlogSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
public class BlogController {
    private final BlogSearchService blogSearchService;

    @Autowired
    public BlogController(BlogSearchService blogSearchService) {
        this.blogSearchService = blogSearchService;
    }

    @GetMapping(value = "/blog/search")
    public ResponseEntity<TransactionResult<BlogSearchResp>> blogSearch(@Valid @ModelAttribute BlogSearchReq blogSearchReq) throws Exception {
        TransactionCode transactionCode = TransactionCode.findByProcessCode("A0000");
        TransactionResult<BlogSearchResp> transactionResult = new TransactionResult<>(transactionCode.getSuccessFail(),
                                                                                        transactionCode.getTransactionCode(),
                                                                                        transactionCode.getResultMessage(),
                                                                                         blogSearchService.blogSearch(blogSearchReq));
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(transactionResult, headers, HttpStatus.OK);
    }
}
