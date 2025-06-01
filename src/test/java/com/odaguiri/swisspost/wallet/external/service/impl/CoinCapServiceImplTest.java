package com.odaguiri.swisspost.wallet.external.service.impl;

import com.odaguiri.swisspost.wallet.external.CoinCapApiProperties;
import com.odaguiri.swisspost.wallet.external.dto.CoinCapApiResponse;
import com.odaguiri.swisspost.wallet.external.dto.CryptoAssetResponse;
import com.odaguiri.swisspost.wallet.external.dto.HistoryPriceResponse;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.exception.CryptoNotFoundException;
import com.odaguiri.swisspost.wallet.service.exception.HistoryPriceUnavailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CoinCapServiceImplTest {

    @Mock
    private RestClient cryptoPriceRestClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private CoinCapApiProperties coinCapApiProperties;

    private CoinCapService coinCapService;

    private final String symbol = "BTC";
    private final String slug = "bitcoin";

    @BeforeEach
    void setUp() {
        coinCapService = new CoinCapServiceImpl(cryptoPriceRestClient, coinCapApiProperties);
        when(cryptoPriceRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    void getAsset_shouldReturnAsset_whenFound() {
        CryptoAssetResponse expectedAsset = new CryptoAssetResponse(slug, symbol, "Bitcoin", new BigDecimal("50000.00"));
        CoinCapApiResponse<List<CryptoAssetResponse>> response = new CoinCapApiResponse<>();
        response.setData(List.of(expectedAsset));

        when(cryptoPriceRestClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(Function.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        CryptoAssetResponse result = coinCapService.getAsset(symbol);

        assertThat(result).isEqualTo(expectedAsset);
        verify(cryptoPriceRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(any(Function.class));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).body(any(ParameterizedTypeReference.class));
    }

    @Test
    void getAsset_shouldThrowException_whenSymbolNotFound() {
        CoinCapApiResponse<List<CryptoAssetResponse>> response = new CoinCapApiResponse<>();
        response.setData(List.of());

        when(cryptoPriceRestClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(Function.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        assertThatThrownBy(() -> coinCapService.getAsset(symbol))
                .isInstanceOf(CryptoNotFoundException.class)
                .hasMessage("Crypto not found: " + symbol);
    }

    @Test
    void getCurrentPrice_shouldReturnPrice_whenFound() {
        BigDecimal expectedPrice = new BigDecimal("50000.00");
        CryptoAssetResponse asset = new CryptoAssetResponse(slug, symbol, "Bitcoin", expectedPrice);
        CoinCapApiResponse<CryptoAssetResponse> response = new CoinCapApiResponse<>();
        response.setData(asset);

        when(requestHeadersUriSpec.uri(any(), eq("bitcoin"))).thenReturn(requestHeadersSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        BigDecimal result = coinCapService.getCurrentPrice(slug);

        assertThat(result).isEqualTo(expectedPrice);
        verify(requestHeadersUriSpec, times(1)).uri(any(), eq("bitcoin"));
        verify(responseSpec, times(1)).body(any(ParameterizedTypeReference.class));
    }

    @Test
    void getCurrentPrice_shouldThrowException_whenSlugNotFound() {
        String invalidSlug = "fiat-money";

        when(cryptoPriceRestClient.get()).thenReturn(requestHeadersUriSpec);
        when(coinCapApiProperties.getSingleAsset()).thenReturn("/assets/{slug}");
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(anyString(), eq(invalidSlug));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(null);

        assertThatThrownBy(() -> coinCapService.getCurrentPrice(invalidSlug))
                .isInstanceOf(CryptoNotFoundException.class)
                        .hasMessage("Crypto not found: " + invalidSlug);
    }

    @Test
    void getPriceAtDate_shouldReturnPrice_whenFound() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        BigDecimal expectedPrice = new BigDecimal("45000.00");
        long timestamp = date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        HistoryPriceResponse historyPrice = new HistoryPriceResponse(expectedPrice, timestamp, date);
        CoinCapApiResponse<List<HistoryPriceResponse>> response = new CoinCapApiResponse<>();
        response.setData(List.of(historyPrice));

        when(cryptoPriceRestClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(Function.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        BigDecimal result = coinCapService.getPriceAtDate(slug, date);

        assertThat(result).isEqualTo(expectedPrice);
        verify(cryptoPriceRestClient, times(1)).get();
        verify(requestHeadersUriSpec, times(1)).uri(any(Function.class));
        verify(requestHeadersSpec, times(1)).retrieve();
        verify(responseSpec, times(1)).body(any(ParameterizedTypeReference.class));
    }

    @Test
    void getPriceAtDate_shouldThrowException_whenPriceNotAvailable() {
        LocalDate date = LocalDate.of(2024, 1, 1);
        CoinCapApiResponse<List<HistoryPriceResponse>> response = new CoinCapApiResponse<>();
        response.setData(List.of());

        when(cryptoPriceRestClient.get()).thenReturn(requestHeadersUriSpec);
        doReturn(requestHeadersSpec).when(requestHeadersUriSpec).uri(any(Function.class));
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        assertThatThrownBy(() -> coinCapService.getPriceAtDate(slug, date))
                .isInstanceOf(HistoryPriceUnavailableException.class)
                .hasMessage("History price unavailable for " + slug + " on " + date);
    }

    @Test
    void validatePrice_shouldReturnTrue_whenPricesMatch() {
        BigDecimal price = new BigDecimal("50000.00");
        CryptoAssetResponse asset = new CryptoAssetResponse("bitcoin", symbol, "Bitcoin", price);
        CoinCapApiResponse<CryptoAssetResponse> response = new CoinCapApiResponse<>();
        response.setData(asset);
        when(requestHeadersUriSpec.uri(any(), eq("bitcoin"))).thenReturn(requestHeadersSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        boolean result = coinCapService.validatePrice(slug, price);

        assertThat(result).isTrue();
        verify(requestHeadersUriSpec, times(1)).uri(any(), eq("bitcoin"));
        verify(responseSpec, times(1)).body(any(ParameterizedTypeReference.class));
    }

    @Test
    void validatePrice_shouldReturnFalse_whenPricesDontMatch() {
        BigDecimal currentPrice = new BigDecimal("50000.00");
        BigDecimal userPrice = new BigDecimal("45000.00");
        CryptoAssetResponse asset = new CryptoAssetResponse("bitcoin", symbol, "Bitcoin", currentPrice);
        CoinCapApiResponse<CryptoAssetResponse> response = new CoinCapApiResponse<>();
        response.setData(asset);
        when(requestHeadersUriSpec.uri(any(), eq("bitcoin"))).thenReturn(requestHeadersSpec);
        when(responseSpec.body(any(ParameterizedTypeReference.class))).thenReturn(response);

        boolean result = coinCapService.validatePrice(slug, userPrice);

        assertThat(result).isFalse();
        verify(requestHeadersUriSpec, times(1)).uri(any(), eq("bitcoin"));
        verify(responseSpec, times(1)).body(any(ParameterizedTypeReference.class));
    }
}
