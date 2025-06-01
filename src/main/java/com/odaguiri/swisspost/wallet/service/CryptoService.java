package com.odaguiri.swisspost.wallet.service;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.service.dto.CryptoDto;

public interface CryptoService {
    CryptoDto createCrypto(CryptoDto createDto);
    Crypto findById(String id);
    Crypto getOrCreateCrypto(String symbol);
    Crypto findBySymbol(String symbol);
}
