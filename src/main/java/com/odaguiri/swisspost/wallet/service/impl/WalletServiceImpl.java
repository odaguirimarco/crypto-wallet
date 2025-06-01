package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.Asset;
import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import com.odaguiri.swisspost.wallet.domain.model.Wallet;
import com.odaguiri.swisspost.wallet.domain.repository.WalletRepository;
import com.odaguiri.swisspost.wallet.security.AuthorizationService;
import com.odaguiri.swisspost.wallet.service.AssetService;
import com.odaguiri.swisspost.wallet.service.ExchangeUserService;
import com.odaguiri.swisspost.wallet.service.WalletService;
import com.odaguiri.swisspost.wallet.service.dto.WalletDto;
import com.odaguiri.swisspost.wallet.service.exception.UnauthorizedOperationException;
import com.odaguiri.swisspost.wallet.service.exception.WalletAlreadyExistsException;
import com.odaguiri.swisspost.wallet.service.exception.WalletNotFoundException;
import com.odaguiri.swisspost.wallet.service.mapper.WalletMapper;
import com.odaguiri.swisspost.wallet.web.dto.WalletCreateRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final ExchangeUserService exchangeUserService;
    private final AuthorizationService authorizationService;
    private final AssetService assetService;

    public WalletServiceImpl(WalletRepository walletRepository,
                             WalletMapper walletMapper,
                             ExchangeUserService exchangeUserService,
                             AuthorizationService authorizationService, AssetService assetService) {
        this.walletRepository = walletRepository;
        this.walletMapper = walletMapper;
        this.exchangeUserService = exchangeUserService;
        this.authorizationService = authorizationService;
        this.assetService = assetService;
    }

    @Override
    public WalletDto createWallet(WalletCreateRequest createDto) {
        if (!authorizationService.isCurrentUser(createDto.email())) {
            throw new UnauthorizedOperationException("Cannot create wallet for another user");
        }

        ExchangeUser exchangeUser = exchangeUserService.findByEmail(createDto.email());
        Wallet wallet = walletRepository.findByExchangeUser(exchangeUser);

        if (wallet != null) {
            throw new WalletAlreadyExistsException(createDto.email());
        }

        wallet = new Wallet(exchangeUser);

        return walletMapper.toDto(walletRepository.save(wallet));
    }

    @Override
    public WalletDto getWallet(Long walletId) {
        Wallet wallet = findWalletById(walletId);
        return walletMapper.toDto(wallet);
    }

    @Override
    public Wallet findWalletById(Long walletId) {
        return walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException(walletId));
    }

    @Override
    public WalletDto addOrUpdateAsset(Long walletId, String symbol, BigDecimal amount, BigDecimal price) {
        Wallet wallet = findWalletById(walletId);

        Asset newAsset = assetService.prepareAsset(symbol, amount, price);
        wallet.upsertAsset(newAsset);

        return walletMapper.toDto(walletRepository.save(wallet));
    }
}
