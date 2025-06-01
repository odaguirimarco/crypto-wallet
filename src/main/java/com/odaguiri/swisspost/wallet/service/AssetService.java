package com.odaguiri.swisspost.wallet.service;

import com.odaguiri.swisspost.wallet.domain.model.Asset;

import java.math.BigDecimal;

public interface AssetService {
    Asset prepareAsset(String assetSlug, BigDecimal amount, BigDecimal price);
    void updateAssetValues(String symbol, BigDecimal newPrice);
}
