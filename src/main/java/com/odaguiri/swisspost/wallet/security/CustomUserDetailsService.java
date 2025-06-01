package com.odaguiri.swisspost.wallet.security;

import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import com.odaguiri.swisspost.wallet.service.impl.ExchangeUserServiceImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final ExchangeUserServiceImpl exchangeUserService;

    public CustomUserDetailsService(ExchangeUserServiceImpl exchangeUserService) {
        this.exchangeUserService = exchangeUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ExchangeUser exchangeUser = exchangeUserService.findByEmail(username);
        return User.builder()
                .username(exchangeUser.getEmail())
                .password(exchangeUser.getPassword())
                .build();
    }
}
