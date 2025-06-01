package com.odaguiri.swisspost.wallet.service.exception;

public class CryptoNotFoundException extends RuntimeException {
    public CryptoNotFoundException(String symbol) {
        super("Crypto not found: " + symbol);
    }

    public String getErrorCode() {
        return "CRYPTO_NOT_FOUND";
    }
}
