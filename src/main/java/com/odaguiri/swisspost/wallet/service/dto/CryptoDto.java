package com.odaguiri.swisspost.wallet.service.dto;

import java.math.BigDecimal;

public record CryptoDto(String id, String symbol, String name, BigDecimal currentPrice) {
}
