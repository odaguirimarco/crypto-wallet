package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import com.odaguiri.swisspost.wallet.web.dto.AssetInput;
import com.odaguiri.swisspost.wallet.web.dto.AssetPerformance;
import com.odaguiri.swisspost.wallet.web.dto.SimulationRequest;
import com.odaguiri.swisspost.wallet.web.dto.SimulationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletSimulationServiceImplTest {

    @Mock
    private CoinCapService coinCapService;
    @Mock
    private CryptoService cryptoService;

    private WalletSimulationServiceImpl walletSimulationService;

    @BeforeEach
    void setUp() {
        walletSimulationService = new WalletSimulationServiceImpl(coinCapService, cryptoService);
    }

    @Test
    void simulateWalletPerformance_shouldReturnSimulationResponse_whenSimulationRequestIsValid() {
        LocalDate simulationDate = LocalDate.of(2024, 1, 1);
        List<AssetInput> assets = List.of(
                new AssetInput("BTC", new BigDecimal("2"), new BigDecimal("50000")),// 2*50000 = 100000
                new AssetInput("ETH", new BigDecimal("10"), new BigDecimal("3000"))// 10*3000 = 30000
        );
        SimulationRequest request = new SimulationRequest(assets, simulationDate);

        Crypto btc = new Crypto("bitcoin", "BTC", "bitcoin", new BigDecimal("50000"));
        Crypto eth = new Crypto("ethereum", "ETH", "ethereum", new BigDecimal("3000"));

        when(cryptoService.getOrCreateCrypto("BTC")).thenReturn(btc);
        when(cryptoService.getOrCreateCrypto("ETH")).thenReturn(eth);
        when(coinCapService.getPriceAtDate(eq("bitcoin"), any())).thenReturn(new BigDecimal("55000"));
        when(coinCapService.getPriceAtDate(eq("ethereum"), any())).thenReturn(new BigDecimal("3030"));

        SimulationResponse response = walletSimulationService.simulateWalletPerformance(request);

        assertThat(response.totalValue()).isEqualByComparingTo(new BigDecimal("140300"));
        assertThat(response.bestAsset()).isEqualTo("BTC");
        assertThat(response.bestPerformance()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(response.worstAsset()).isEqualTo("ETH");
        assertThat(response.worstPerformance()).isEqualByComparingTo(new BigDecimal("1"));
        assertThat(response.assetPerformances()).hasSize(2);
        verify(cryptoService).getOrCreateCrypto("BTC");
        verify(cryptoService).getOrCreateCrypto("ETH");
        verify(coinCapService).getPriceAtDate(eq("bitcoin"), any());
        verify(coinCapService).getPriceAtDate(eq("ethereum"), any());
    }

    @Test
    void calculatePercentageChange_shouldReturnPercentageChange_whenInitialAndCurrentValueAreValid() {
        BigDecimal initialValue = new BigDecimal("100");
        BigDecimal currentValue = new BigDecimal("110");

        BigDecimal result = walletSimulationService.calculatePercentageChange(initialValue, currentValue);

        assertThat(result).isEqualByComparingTo(new BigDecimal("10.00"));
    }

    @Test
    void buildResponse_shouldReturnSimulationResponse_whenPerformancesAreValid() {
        List<AssetPerformance> performances = List.of(
                new AssetPerformance(
                        "BTC",
                        new BigDecimal("100000"),
                        new BigDecimal("110000"),
                        new BigDecimal("10")),
                new AssetPerformance(
                        "ETH",
                        new BigDecimal("1000"),
                        new BigDecimal("1010"),
                        new BigDecimal("1"))
        );

        SimulationResponse response = walletSimulationService.buildResponse(performances);

        assertThat(response.totalValue()).isEqualByComparingTo(new BigDecimal("111010"));
        assertThat(response.bestAsset()).isEqualTo("BTC");
        assertThat(response.bestPerformance()).isEqualByComparingTo(new BigDecimal("10"));
        assertThat(response.worstAsset()).isEqualTo("ETH");
        assertThat(response.worstPerformance()).isEqualByComparingTo(new BigDecimal("1"));
        assertThat(response.assetPerformances()).hasSize(2);
    }
}
