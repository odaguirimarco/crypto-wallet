package com.odaguiri.swisspost.wallet.service.exception;

import java.math.BigDecimal;

public class InvalidPriceException extends RuntimeException {
    public InvalidPriceException(String symbol, BigDecimal userProvidedPrice) {
        super("Invalid price for " + symbol + ": " + userProvidedPrice);
    }

    public String getErrorCode() {
        return "INVALID_PRICE";
    }
}
