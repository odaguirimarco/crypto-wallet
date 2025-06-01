package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.domain.repository.CryptoRepository;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import com.odaguiri.swisspost.wallet.service.exception.CryptoNotFoundException;
import com.odaguiri.swisspost.wallet.service.mapper.CryptoMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CryptoServiceImplTest {

    @Mock
    private CryptoRepository cryptoRepository;
    @Mock
    private CoinCapService coinCapService;
    @Mock
    private CryptoMapper cryptoMapper;

    private CryptoService cryptoService;

    private final String id = "bitcoin";
    private final String symbol = "BTC";

    @BeforeEach
    void setUp() {
        cryptoService = new CryptoServiceImpl(cryptoRepository, cryptoMapper, coinCapService);
    }

    @Test
    void findBySymbol_shouldReturnCrypto_whenCryptoExists() {
        BigDecimal currentPrice = new BigDecimal("10000.00");
        Crypto expectedCrypto = new Crypto(id, symbol, id, currentPrice);
        when(cryptoRepository.findBySymbol(symbol)).thenReturn(Optional.of(expectedCrypto));

        Crypto result = cryptoService.findBySymbol(symbol);

        assertThat(result).isNotNull();
        assertThat(result.getSymbol()).isEqualTo(symbol);
        assertThat(result.getId()).isEqualTo(id);
        verify(cryptoRepository).findBySymbol(symbol);
    }

    @Test
    void findBySymbol_shouldThrowException_whenCryptoNotExists() {
        when(cryptoRepository.findBySymbol(symbol)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cryptoService.findBySymbol(symbol))
                .isInstanceOf(CryptoNotFoundException.class)
                .hasMessage("Crypto not found: " + symbol);
    }

    @Test
    void findOrCreate_shouldReturnExistingCrypto_whenCryptoExists() {
        BigDecimal currentPrice = new BigDecimal("10000.00");
        Crypto expectedCrypto = new Crypto(id, symbol, id, currentPrice);
        when(cryptoRepository.findBySymbol(symbol)).thenReturn(Optional.of(expectedCrypto));

        Crypto result = cryptoService.getOrCreateCrypto(symbol);

        assertThat(result).isEqualTo(expectedCrypto);
        verify(coinCapService, never()).getAsset(any());
        verify(cryptoRepository, never()).save(any());
        verify(cryptoRepository).findBySymbol(symbol);
    }
}
