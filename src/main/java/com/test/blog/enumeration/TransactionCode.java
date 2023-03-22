package com.test.blog.enumeration;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum TransactionCode {
    A0000("success", "A0000", "정상으로 처리되었어요"),
    A0001("fail", "A0001", "원인을 알 수 없는 오류에요"),
    A0002("fail", "A0002", "정보제공사의 응답 전문 처리를 실패했어요"),
    A0003("fail", "A0003", "정보제공사에 요청을 실패했어요");

    private final String successFail;
    private final String transactionCode;
    private final String resultMessage;

    private TransactionCode(String successFail, String transactionCode, String resultMessage) {
        this.successFail = successFail;
        this.transactionCode = transactionCode;
        this.resultMessage = resultMessage;
    }

    public static TransactionCode findByProcessCode(String inTransactionCode) {
        return Arrays.stream(TransactionCode.values())
                .filter(transactionCode -> transactionCode.getTransactionCode().equals(inTransactionCode))
                .findAny()
                .orElse(A0001);
    }
}
