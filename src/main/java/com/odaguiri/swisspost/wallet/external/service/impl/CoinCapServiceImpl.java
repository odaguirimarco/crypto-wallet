package com.odaguiri.swisspost.wallet.external.service.impl;

import com.odaguiri.swisspost.wallet.external.CoinCapApiProperties;
import com.odaguiri.swisspost.wallet.external.dto.CoinCapApiResponse;
import com.odaguiri.swisspost.wallet.external.dto.CryptoAssetResponse;
import com.odaguiri.swisspost.wallet.external.dto.HistoryPriceResponse;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.exception.CryptoNotFoundException;
import com.odaguiri.swisspost.wallet.service.exception.HistoryPriceUnavailableException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class CoinCapServiceImpl implements CoinCapService {

    private final RestClient cryptoPriceRestClient;
    private final CoinCapApiProperties coinCapApiProperties;

    public CoinCapServiceImpl(RestClient cryptoPriceRestClient, CoinCapApiProperties coinCapApiProperties) {
        this.cryptoPriceRestClient = cryptoPriceRestClient;
        this.coinCapApiProperties = coinCapApiProperties;
    }

    @Override
    public CryptoAssetResponse getAsset(String symbol) {
        CoinCapApiResponse<List<CryptoAssetResponse>> response = cryptoPriceRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(coinCapApiProperties.getAssets())
                        .queryParam("search", symbol)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<CoinCapApiResponse<List<CryptoAssetResponse>>>() {});

        if (response == null || response.getData() == null) {
            throw new CryptoNotFoundException(symbol);
        }

        return response.getData().stream()
                .filter(crypto -> symbol.equals(crypto.symbol()))
                .findFirst()
                .orElseThrow(() -> new CryptoNotFoundException(symbol));
    }

    @Override
    public BigDecimal getCurrentPrice(String slug) {
        CoinCapApiResponse<CryptoAssetResponse> response = cryptoPriceRestClient.get()
                .uri(coinCapApiProperties.getSingleAsset(), slug)
                .retrieve()
                .body(new ParameterizedTypeReference<CoinCapApiResponse<CryptoAssetResponse>>() {});

        if (response == null) {
            throw new CryptoNotFoundException(slug);
        }

        return response.getData().priceUsd();
    }

    @Override
    public BigDecimal getPriceAtDate(String slug, LocalDate date) {
        long startMillis = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
        long endMillis = date.plusDays(1L).atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        CoinCapApiResponse<List<HistoryPriceResponse>> response = cryptoPriceRestClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(coinCapApiProperties.getAssetHistory())
                        .queryParam("interval", "d1")
                        .queryParam("start", startMillis)
                        .queryParam("end", endMillis)
                        .build(slug))
                .retrieve()
                .body(new ParameterizedTypeReference<CoinCapApiResponse<List<HistoryPriceResponse>>>() {});

        if (response == null) {
            throw new CryptoNotFoundException(slug);
        }

        return response.getData().stream()
                .filter(historyPrice -> isSameDay(historyPrice.time(), date))
                .findFirst()
                .map(HistoryPriceResponse::priceUsd)
                .orElseThrow(() -> new HistoryPriceUnavailableException(slug, date));
    }

    /**
     * Validate if the user-provided price is the same as the current price.
     * I am using the integer part because this is for testing purposes,
     * since it is challenging to predict the current price of a crypto.
     * @param slug The name of the crypto to validate the price for. This is used to get the current price from CoinCap.
     *             Example: bitcoin (BTC), solana (SOL), etc.
     * @param userProvidedPrice The price the user provided. This is used to compare with the current price.
     * @return true if the user-provided price is the same as the current price, false otherwise.
     */
    @Override
    public boolean validatePrice(String slug, BigDecimal userProvidedPrice) {
        BigDecimal currentPrice = getCurrentPrice(slug);
        return currentPrice.toBigInteger().compareTo(userProvidedPrice.toBigInteger()) == 0;
    }

    private boolean isSameDay(long timestamp, LocalDate targetDate) {
        return Instant.ofEpochMilli(timestamp).atZone(ZoneOffset.UTC).toLocalDate().isEqual(targetDate);
    }
}
