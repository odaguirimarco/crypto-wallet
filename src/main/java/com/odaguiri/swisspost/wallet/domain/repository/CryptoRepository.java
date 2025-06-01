package com.odaguiri.swisspost.wallet.domain.repository;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoRepository extends CrudRepository<Crypto, String> {
    Optional<Crypto> findBySymbol(String symbol);
}
