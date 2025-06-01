package com.odaguiri.swisspost.wallet.web.dto;

import java.math.BigDecimal;

public record AssetAdditionRequest(
        String symbol,
        BigDecimal price,
        BigDecimal amount
) {}
