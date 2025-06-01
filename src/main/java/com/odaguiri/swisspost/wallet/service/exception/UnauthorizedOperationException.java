package com.odaguiri.swisspost.wallet.service.exception;

public class UnauthorizedOperationException extends RuntimeException {
    public UnauthorizedOperationException(String s) {
        super(s);
    }

    public String getErrorCode() {
        return "UNAUTHORIZED_OPERATION";
    }
}
