package com.odaguiri.swisspost.wallet.web.dto;

import java.math.BigDecimal;

public record AssetInput(
        String symbol,
        BigDecimal quantity,
        BigDecimal value
) {}
