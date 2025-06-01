package com.odaguiri.swisspost.wallet.external.dto;

public class CoinCapApiResponse<T> {
    private T data;
    private long timestamp;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
