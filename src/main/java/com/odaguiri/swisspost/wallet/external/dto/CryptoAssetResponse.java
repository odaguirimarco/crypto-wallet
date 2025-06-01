package com.odaguiri.swisspost.wallet.external.dto;

import java.math.BigDecimal;

public record CryptoAssetResponse(
        String id,
        String symbol,
        String name,
        BigDecimal priceUsd
) {}
