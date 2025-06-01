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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    @Override
    public List<Crypto> findAll() {
        return StreamSupport.stream(cryptoRepository.findAll().spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void updatePrice(String id, BigDecimal newPrice) {
        Crypto crypto = findById(id);
        crypto.setCurrentPrice(newPrice);
        cryptoRepository.save(crypto);
    }
}
