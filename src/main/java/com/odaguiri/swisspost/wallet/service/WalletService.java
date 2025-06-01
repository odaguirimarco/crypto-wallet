package com.odaguiri.swisspost.wallet.service;

import com.odaguiri.swisspost.wallet.domain.model.Wallet;
import com.odaguiri.swisspost.wallet.service.dto.WalletDto;
import com.odaguiri.swisspost.wallet.web.dto.WalletCreateRequest;

import java.math.BigDecimal;

public interface WalletService {
    WalletDto createWallet(WalletCreateRequest createDto);
    WalletDto getWallet(Long walletId);
    Wallet findWalletById(Long walletId);
    WalletDto addOrUpdateAsset(Long walletId, String symbol, BigDecimal amount, BigDecimal price);
}
