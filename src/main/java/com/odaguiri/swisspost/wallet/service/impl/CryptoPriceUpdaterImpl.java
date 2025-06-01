package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.AssetService;
import com.odaguiri.swisspost.wallet.service.CryptoPriceUpdater;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class CryptoPriceUpdaterImpl implements CryptoPriceUpdater {

    private final CryptoService cryptoService;
    private final CoinCapService coinCapService;
    private ExecutorService executor;
    private final AssetService assetService;

    @Value("${app.crypto-updater.batch-size:3}")
    private int batchSize;
    @Value("${app.crypto-updater.interval-ms:300000}")
    private int updateInterval;

    public CryptoPriceUpdaterImpl(CryptoService cryptoService,
                                  CoinCapService coinCapService,
                                  AssetService assetService) {
        this.coinCapService = coinCapService;
        this.cryptoService = cryptoService;
        this.assetService = assetService;
    }

    @PostConstruct
    private void init() {
        this.executor = Executors.newFixedThreadPool(batchSize);
    }

    @PreDestroy
    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }


    @Override
    public void updateAllCryptoPrices() {
        List<Crypto> slugs = cryptoService.findAll();
        for (int i = 0; i < slugs.size(); i += batchSize) {
            int end = Math.min(i + batchSize, slugs.size());
            List<Crypto> batch = slugs.subList(i, end);
            List<CompletableFuture<Void>> futures = batch.stream()
                    .map(crypto -> CompletableFuture.runAsync(
                            () -> updateCryptoPrice(crypto),
                            executor
                    ))
                    .toList();
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        }
    }

    private void updateCryptoPrice(Crypto crypto) {
        BigDecimal newPrice = coinCapService.getCurrentPrice(crypto.getId());
        cryptoService.updatePrice(crypto.getId(), newPrice);
        assetService.updateAssetValues(crypto.getSymbol(), newPrice);
    }
}
