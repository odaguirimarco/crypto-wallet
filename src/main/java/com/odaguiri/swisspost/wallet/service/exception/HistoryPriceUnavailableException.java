package com.odaguiri.swisspost.wallet.service.exception;

import java.time.LocalDate;

public class HistoryPriceUnavailableException extends RuntimeException {
    public HistoryPriceUnavailableException(String slug, LocalDate date) {
        super("History price unavailable for " + slug + " on " + date);
    }

    public String getErrorCode() {
        return "HISTORY_PRICE_UNAVAILABLE";
    }
}
