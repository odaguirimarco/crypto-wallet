package com.odaguiri.swisspost.wallet.service;

import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;

public interface ExchangeUserService {
    ExchangeUser create(ExchangeUser exchangeUser);
    boolean existsByEmail(String email);
    ExchangeUser findByEmail(String email);
}
