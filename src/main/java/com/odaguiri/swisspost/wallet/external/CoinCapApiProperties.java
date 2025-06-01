package com.odaguiri.swisspost.wallet.external;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "app.coincapapi")
public class CoinCapApiProperties {
    private String url;
    private String key;
    private String singleAsset;
    private String assetHistory;
    private String assets;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSingleAsset() {
        return singleAsset;
    }

    public void setSingleAsset(String singleAsset) {
        this.singleAsset = singleAsset;
    }

    public String getAssetHistory() {
        return assetHistory;
    }

    public void setAssetHistory(String assetHistory) {
        this.assetHistory = assetHistory;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }
}
