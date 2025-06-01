package com.odaguiri.swisspost.wallet.service.exception;

public class ExchangeUserNotFoundException extends RuntimeException {
    public ExchangeUserNotFoundException(String email) {
        super("Exchange user not found: " + email);
    }

    public String getErrorCode() {
        return "EXCHANGE_USER_NOT_FOUND";
    }
}
