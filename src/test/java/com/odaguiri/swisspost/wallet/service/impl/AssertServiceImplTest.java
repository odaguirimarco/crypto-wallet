package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Asset;
import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.domain.repository.AssetRepository;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import com.odaguiri.swisspost.wallet.service.exception.InvalidPriceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssertServiceImplTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private CryptoService cryptoService;

    @Mock
    private CoinCapService coinCapService;

    private AssetServiceImpl assetService;

    private final String symbol = "BTC";
    private Crypto crypto;

    @BeforeEach
    void setUp() {
        assetService = new AssetServiceImpl(assetRepository, cryptoService, coinCapService);
        crypto = new Crypto("bitcoin", symbol, "bitcoin", BigDecimal.ZERO);
    }

    @Test
    void prepareAsset_shouldReturnAsset_whenValidPrice() {
        BigDecimal amount = BigDecimal.ONE;
        BigDecimal price = BigDecimal.valueOf(50000);

        when(cryptoService.getOrCreateCrypto(symbol)).thenReturn(crypto);
        when(coinCapService.validatePrice(crypto.getId(), price)).thenReturn(true);

        Asset result = assetService.prepareAsset(symbol, amount, price);

        assertThat(result).isNotNull();
        assertThat(result.getSymbol()).isEqualTo(symbol);
        assertThat(result.getQuantity()).isEqualTo(amount);
        assertThat(result.getPrice()).isEqualTo(price);
    }

    @Test
    void prepareAsset_shouldThrowException_whenInvalidPrice() {
        BigDecimal amount = BigDecimal.ONE;
        BigDecimal price = BigDecimal.valueOf(50000);

        when(cryptoService.getOrCreateCrypto(symbol)).thenReturn(crypto);
        when(coinCapService.validatePrice(crypto.getId(), price)).thenReturn(false);

        assertThatThrownBy(() -> assetService.prepareAsset(symbol, amount, price))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("Invalid price for " + symbol + ": " + price);
    }
}
