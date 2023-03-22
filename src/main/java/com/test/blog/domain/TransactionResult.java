package com.test.blog.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class TransactionResult<O> {
    private String successFail;    	    // 성공실패여부
    private String transactionCode;		// 결과코드
    private String resultMessage;		// 결과메시지
	private O resultObject;				// 결과객체

	@Builder
	public TransactionResult(String successFail, String transactionCode, String resultMessage, O resultObject) {
		super();
		this.successFail = successFail;
		this.transactionCode = transactionCode;
		this.resultMessage = resultMessage;
		this.resultObject = resultObject;
	}
}
