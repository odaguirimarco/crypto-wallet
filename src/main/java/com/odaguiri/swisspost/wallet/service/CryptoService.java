package com.odaguiri.swisspost.wallet.service;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.service.dto.CryptoDto;

import java.math.BigDecimal;
import java.util.List;

public interface CryptoService {
    CryptoDto createCrypto(CryptoDto createDto);
    Crypto findById(String id);
    Crypto getOrCreateCrypto(String symbol);
    Crypto findBySymbol(String symbol);
    List<Crypto> findAll();
    void updatePrice(String id, BigDecimal newPrice);
}
