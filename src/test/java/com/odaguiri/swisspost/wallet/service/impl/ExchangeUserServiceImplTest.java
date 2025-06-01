package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import com.odaguiri.swisspost.wallet.domain.repository.ExchangeUserRepository;
import com.odaguiri.swisspost.wallet.service.exception.ExchangeUserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExchangeUserServiceImplTest {

    @Mock
    private ExchangeUserRepository exchangeUserRepository;

    private ExchangeUserServiceImpl exchangeUserService;

    private final String email = "user@example.com";
    private final String encodedPassword = "encodedPassword";

    @BeforeEach
    void setUp() {
        exchangeUserService = new ExchangeUserServiceImpl(exchangeUserRepository);
    }

    @Test
    void createUser_shouldCreateNewUser_whenUserDoesNotExist() {
        ExchangeUser expectedUser = new ExchangeUser(email, encodedPassword);
        when(exchangeUserRepository.save(any(ExchangeUser.class))).thenReturn(expectedUser);

        ExchangeUser result = exchangeUserService.create(expectedUser);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        verify(exchangeUserRepository).save(any(ExchangeUser.class));
    }

    @Test
    void existsByEmail_shouldReturnTrue_whenExchangeUserExists() {
        when(exchangeUserRepository.existsByEmail(email)).thenReturn(true);

        boolean result = exchangeUserService.existsByEmail(email);

        assertThat(result).isTrue();
        verify(exchangeUserRepository).existsByEmail(email);
    }

    @Test
    void existsByEmail_shouldReturnFalse_whenExchangeUserNotFound() {
        when(exchangeUserRepository.existsByEmail(email)).thenReturn(false);

        boolean result = exchangeUserService.existsByEmail(email);

        assertThat(result).isFalse();
        verify(exchangeUserRepository).existsByEmail(email);
    }

    @Test
    void findByEmail_shouldReturnExchangeUser_whenExchangeUserExists() {
        ExchangeUser expectedUser = new ExchangeUser(email, encodedPassword);
        when(exchangeUserRepository.findByEmail(email)).thenReturn(Optional.of(expectedUser));

        ExchangeUser result = exchangeUserService.findByEmail(email);

        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPassword()).isEqualTo(encodedPassword);
        verify(exchangeUserRepository).findByEmail(email);
    }

    @Test
    void findByEmail_shouldThrowsException_whenExchangeUserNotFound() {
        when(exchangeUserRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> exchangeUserService.findByEmail(email))
                .isInstanceOf(ExchangeUserNotFoundException.class)
                .hasMessage("Exchange user not found: " + email);
    }
}
