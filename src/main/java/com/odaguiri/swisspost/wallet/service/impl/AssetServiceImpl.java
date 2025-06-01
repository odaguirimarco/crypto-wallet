package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Asset;
import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.domain.repository.AssetRepository;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.AssetService;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import com.odaguiri.swisspost.wallet.service.exception.InvalidPriceException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;
    private final CryptoService cryptoService;
    private final CoinCapService coinCapService;

    public AssetServiceImpl(AssetRepository assetRepository,
                            CryptoService cryptoService,
                            CoinCapService coinCapService) {
        this.assetRepository = assetRepository;
        this.cryptoService = cryptoService;
        this.coinCapService = coinCapService;
    }

    @Override
    public Asset prepareAsset(String symbol, BigDecimal amount, BigDecimal price) {
        Crypto crypto = cryptoService.getOrCreateCrypto(symbol);
        if (!coinCapService.validatePrice(crypto.getId(), price)) {
            throw new InvalidPriceException(symbol, price);
        }
        return new Asset(symbol, amount, price);
    }

    @Override
    public void updateAssetValues(String symbol, BigDecimal newPrice) {
        assetRepository.updateAssetValues(symbol, newPrice);
    }
}
