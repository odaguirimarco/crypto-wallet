package com.odaguiri.swisspost.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.List;

public record SimulationResponse(
        @JsonProperty("total") BigDecimal totalValue,
        @JsonProperty("best_asset") String bestAsset,
        @JsonProperty("best_performance") BigDecimal bestPerformance,
        @JsonProperty("worst_asset") String worstAsset,
        @JsonProperty("worst_performance") BigDecimal worstPerformance,
        @JsonProperty("assets_performance") List<AssetPerformance> assetPerformances
) {}
