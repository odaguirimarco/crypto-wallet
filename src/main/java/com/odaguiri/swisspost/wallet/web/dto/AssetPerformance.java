package com.odaguiri.swisspost.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record AssetPerformance(
        @JsonProperty("symbol") String symbol,
        @JsonProperty("initial_value") BigDecimal initialValue,
        @JsonProperty("current_value") BigDecimal currentValue,
        @JsonProperty("performance_percentage") BigDecimal performancePercentage
) {}
