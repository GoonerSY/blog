package com.test.blog.exception;

public class BadWebClientRequestException extends RuntimeException {

    private static final long serialVersionUID = -2981521843508024920L;
    private final int statusCode;

    private String statusText;

    public BadWebClientRequestException(int statusCode, String msg) {
        super(msg);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
