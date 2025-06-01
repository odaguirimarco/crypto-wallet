package com.odaguiri.swisspost.wallet.service.impl;

import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import com.odaguiri.swisspost.wallet.domain.repository.ExchangeUserRepository;
import com.odaguiri.swisspost.wallet.service.ExchangeUserService;
import com.odaguiri.swisspost.wallet.service.exception.ExchangeUserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ExchangeUserServiceImpl implements ExchangeUserService {

    private final ExchangeUserRepository exchangeUserRepository;

    public ExchangeUserServiceImpl(ExchangeUserRepository exchangeUserRepository) {
        this.exchangeUserRepository = exchangeUserRepository;
    }

    @Override
    public ExchangeUser create(ExchangeUser exchangeUser) {
        return exchangeUserRepository.save(exchangeUser);
    }

    @Override
    public boolean existsByEmail(String email) {
        return exchangeUserRepository.existsByEmail(email);
    }

    @Override
    public ExchangeUser findByEmail(String email) {
        return exchangeUserRepository
                .findByEmail(email)
                .orElseThrow(() -> new ExchangeUserNotFoundException(email));
    }
}
