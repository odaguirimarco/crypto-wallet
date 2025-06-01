package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.domain.repository.CryptoRepository;
import com.odaguiri.swisspost.wallet.external.dto.CryptoAssetResponse;
import com.odaguiri.swisspost.wallet.external.service.CoinCapService;
import com.odaguiri.swisspost.wallet.service.CryptoService;
import com.odaguiri.swisspost.wallet.service.dto.CryptoDto;
import com.odaguiri.swisspost.wallet.service.exception.CryptoNotFoundException;
import com.odaguiri.swisspost.wallet.service.mapper.CryptoMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CryptoServiceImpl implements CryptoService {

    private final CryptoRepository cryptoRepository;
    private final CryptoMapper cryptoMapper;
    private final CoinCapService coinCapService;

    public CryptoServiceImpl(CryptoRepository cryptoRepository,
                             CryptoMapper cryptoMapper,
                             CoinCapService coinCapService) {
        this.cryptoRepository = cryptoRepository;
        this.cryptoMapper = cryptoMapper;
        this.coinCapService = coinCapService;
    }

    @Override
    public CryptoDto createCrypto(CryptoDto createDto) {
        Crypto crypto = cryptoRepository.save(cryptoMapper.toEntity(createDto));
        return cryptoMapper.toDto(crypto);
    }

    @Override
    public Crypto findById(String id) {
        return cryptoRepository.findById(id)
                .orElseThrow(() -> new CryptoNotFoundException(id));
    }

    @Override
    public Crypto getOrCreateCrypto(String symbol) {
        Optional<Crypto> crypto = cryptoRepository.findBySymbol(symbol);
        if (crypto.isPresent()) {
            return crypto.get();
        }

        CryptoAssetResponse assetResponse = coinCapService.getAsset(symbol);
        if (assetResponse == null) {
            throw new CryptoNotFoundException(symbol);
        }

        CryptoDto newCryptoDto = new CryptoDto(
                assetResponse.id(),
                assetResponse.symbol(),
                assetResponse.name(),
                assetResponse.priceUsd()
        );

        CryptoDto savedCrypto = createCrypto(newCryptoDto);
        return cryptoMapper.toEntity(savedCrypto);
    }

    @Override
    public Crypto findBySymbol(String symbol) {
        return cryptoRepository.findBySymbol(symbol)
                .orElseThrow(() -> new CryptoNotFoundException(symbol));
    }
}
