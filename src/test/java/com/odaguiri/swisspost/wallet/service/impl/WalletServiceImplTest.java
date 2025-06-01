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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WalletServiceImplTest {

    @Mock
    private WalletRepository walletRepository;
    @Mock
    private WalletMapper walletMapper;
    @Mock
    private ExchangeUserService exchangeUserService;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private AssetService assetService;

    private WalletService walletService;

    private final String email = "user@example.com";
    private final String password = "password";

    @BeforeEach
    void setUp() {
        walletService = new WalletServiceImpl(
                walletRepository,
                walletMapper,
                exchangeUserService,
                authorizationService,
                assetService);
    }

    @Test
    void createWallet_shouldCreateNewWallet_whenUserDoesNotExist() {
        ExchangeUser user = new ExchangeUser(email, password);
        Wallet wallet = new Wallet(user);
        WalletDto expectedDto = new WalletDto(1L, BigDecimal.ZERO, List.of());
        WalletCreateRequest request = new WalletCreateRequest(email);

        when(authorizationService.isCurrentUser(email)).thenReturn(true);
        when(exchangeUserService.findByEmail(email)).thenReturn(user);
        when(walletRepository.findByExchangeUser(user)).thenReturn(null);
        when(walletRepository.save(any(Wallet.class))).thenReturn(wallet);
        when(walletMapper.toDto(wallet)).thenReturn(expectedDto);

        WalletDto result = walletService.createWallet(request);

        assertThat(result).isEqualTo(expectedDto);
        verify(authorizationService).isCurrentUser(email);
        verify(exchangeUserService).findByEmail(email);
        verify(walletRepository).findByExchangeUser(user);
        verify(walletRepository).save(any(Wallet.class));
        verify(walletMapper).toDto(wallet);
    }

    @Test
    void createWallet_shouldThrowException_whenUserNotFound() {
        WalletCreateRequest request = new WalletCreateRequest(email);
        when(authorizationService.isCurrentUser(email)).thenReturn(false);

        assertThatThrownBy(() -> walletService.createWallet(request))
                .isInstanceOf(UnauthorizedOperationException.class)
                .hasMessage("Cannot create wallet for another user");
    }

    @Test
    void createWallet_shouldThrowException_whenWalletAlreadyExists() {
        ExchangeUser user = new ExchangeUser(email, password);
        Wallet existingWallet = new Wallet(user);
        WalletCreateRequest request = new WalletCreateRequest(email);

        when(authorizationService.isCurrentUser(email)).thenReturn(true);
        when(exchangeUserService.findByEmail(email)).thenReturn(user);
        when(walletRepository.findByExchangeUser(user)).thenReturn(existingWallet);

        assertThatThrownBy(() -> walletService.createWallet(request))
                .isInstanceOf(WalletAlreadyExistsException.class)
                .hasMessage("Wallet with email " + email + " already exists");

    }

    @Test
    void getWallet_shouldReturnWallet_whenWalletExists() {
        Long walletId = 1L;
        ExchangeUser user = new ExchangeUser(email, password);
        Wallet wallet = new Wallet(user);
        WalletDto expectedDto = new WalletDto(walletId, BigDecimal.ZERO, List.of());

        when(walletRepository.findById(walletId)).thenReturn(Optional.of(wallet));
        when(walletMapper.toDto(wallet)).thenReturn(expectedDto);

        WalletDto result = walletService.getWallet(walletId);

        assertThat(result).isEqualTo(expectedDto);
        verify(walletRepository).findById(walletId);
        verify(walletMapper).toDto(wallet);
    }

    @Test
    void getWallet_shouldThrowException_whenWalletNotFound() {
        Long walletId = 1L;
        when(walletRepository.findById(walletId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> walletService.getWallet(walletId))
                .isInstanceOf(WalletNotFoundException.class)
                .hasMessage("Wallet not found: " + walletId);
    }

    @Test
    void addOrUpdateAsset_shouldAddAsset_whenAssetDoesNotExist() {
        Long walletId = 1L;
        String symbol = "BTC";
        BigDecimal amount = BigDecimal.ONE;
        BigDecimal price = BigDecimal.valueOf(50000);
        ExchangeUser user = new ExchangeUser(email, password);
        Wallet wallet = new Wallet(user);
        Asset asset = new Asset(symbol, amount, price);
        WalletDto expectedDto = new WalletDto(walletId, BigDecimal.ZERO, List.of());

        when(walletRepository.findById(walletId)).thenReturn(java.util.Optional.of(wallet));
        when(assetService.prepareAsset(symbol, amount, price)).thenReturn(asset);
        when(walletRepository.save(wallet)).thenReturn(wallet);
        when(walletMapper.toDto(wallet)).thenReturn(expectedDto);

        WalletDto result = walletService.addOrUpdateAsset(walletId, symbol, amount, price);

        assertThat(result).isEqualTo(expectedDto);
        verify(walletRepository).findById(walletId);
        verify(assetService).prepareAsset(symbol, amount, price);
        verify(walletRepository).save(wallet);
        verify(walletMapper).toDto(wallet);
    }
}
