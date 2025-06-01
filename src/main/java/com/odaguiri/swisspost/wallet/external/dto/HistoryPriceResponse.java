package com.odaguiri.swisspost.wallet.external.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoryPriceResponse(
        BigDecimal priceUsd,
        long time,
        LocalDate date
) {}
