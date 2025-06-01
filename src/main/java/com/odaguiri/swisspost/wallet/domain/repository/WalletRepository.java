package com.odaguiri.swisspost.wallet.domain.repository;

import com.odaguiri.swisspost.wallet.domain.model.Wallet;
import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletRepository extends CrudRepository<Wallet, Long> {
    Wallet findByExchangeUser(ExchangeUser user);
}
