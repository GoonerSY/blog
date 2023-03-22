package com.test.blog.controller;

import com.test.blog.domain.TransactionResult;
import com.test.blog.domain.TrendSearchReq;
import com.test.blog.domain.TrendSearchResp;
import com.test.blog.enumeration.TransactionCode;
import com.test.blog.service.TrendSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
public class TrendController {

    private final TrendSearchService trendSearchService;

    @Autowired
    public TrendController(TrendSearchService trendSearchService) {
        this.trendSearchService = trendSearchService;
    }

    @GetMapping(value = "/trend/search")
    public ResponseEntity<TransactionResult<TrendSearchResp>> trendSearch(@Valid @ModelAttribute TrendSearchReq trendSearchReq) throws Exception {
        TransactionCode transactionCode = TransactionCode.findByProcessCode("A0000");
        TransactionResult<TrendSearchResp> transactionResult = new TransactionResult<>(transactionCode.getSuccessFail(),
                                                                                        transactionCode.getTransactionCode(),
                                                                                        transactionCode.getResultMessage(),
                                                                                        trendSearchService.trendSearch(trendSearchReq));
        HttpHeaders headers= new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
        return new ResponseEntity<>(transactionResult, headers, HttpStatus.OK);
    }
}
