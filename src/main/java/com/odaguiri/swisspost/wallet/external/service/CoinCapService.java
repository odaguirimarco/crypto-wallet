package com.odaguiri.swisspost.wallet.external.service;

import com.odaguiri.swisspost.wallet.external.dto.CryptoAssetResponse;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CoinCapService {
    CryptoAssetResponse getAsset(String symbol);
    BigDecimal getCurrentPrice(String slug);
    BigDecimal getPriceAtDate(String slug, LocalDate date);
    boolean validatePrice(String slug, BigDecimal userProvidedPrice);
}
