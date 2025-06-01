package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import com.odaguiri.swisspost.wallet.service.WalletSimulationService;
import com.odaguiri.swisspost.wallet.web.dto.AssetInput;
import com.odaguiri.swisspost.wallet.web.dto.AssetPerformance;
import com.odaguiri.swisspost.wallet.web.dto.SimulationRequest;
import com.odaguiri.swisspost.wallet.web.dto.SimulationResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class WalletSimulationServiceImpl implements WalletSimulationService {

    private final CoinCapService coinCapService;
    private final CryptoService cryptoService;

    public WalletSimulationServiceImpl(CoinCapService coinCapService, CryptoService cryptoService) {
        this.coinCapService = coinCapService;
        this.cryptoService = cryptoService;
    }

    @Override
    public SimulationResponse simulateWalletPerformance(SimulationRequest request) {
        List<AssetPerformance> performances = request.assets().stream()
                .map(asset -> calculateAssetPerformance(asset, request.simulationDate()))
                .toList();
        return buildResponse(performances);
    }

    protected AssetPerformance calculateAssetPerformance(AssetInput asset, LocalDate simulationDate) {

        Crypto crypto = cryptoService.getOrCreateCrypto(asset.symbol());

        BigDecimal historyPrice = coinCapService.getPriceAtDate(crypto.getId(), simulationDate);

        BigDecimal userValue = asset.quantity().multiply(asset.value());
        BigDecimal historyValue = asset.quantity().multiply(historyPrice);
        BigDecimal performance = calculatePercentageChange(userValue, historyValue);

        return new AssetPerformance(
                asset.symbol(),
                userValue,
                historyValue,
                performance
        );
    }

    protected BigDecimal calculatePercentageChange(BigDecimal initialValue, BigDecimal currentValue) {
        return currentValue.subtract(initialValue)
                .divide(initialValue, 6, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(100))
                .setScale(2, RoundingMode.HALF_UP);
    }

    protected SimulationResponse buildResponse(List<AssetPerformance> performances) {
        BigDecimal totalValue = performances.stream()
                .map(AssetPerformance::currentValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        AssetPerformance best = performances.stream()
                .max(Comparator.comparing(AssetPerformance::performancePercentage))
                .orElseThrow();

        AssetPerformance worst = performances.stream()
                .min(Comparator.comparing(AssetPerformance::performancePercentage))
                .orElseThrow();

        return new SimulationResponse(
                totalValue,
                best.symbol(),
                best.performancePercentage(),
                worst.symbol(),
                worst.performancePercentage(),
                performances
        );
    }
}
