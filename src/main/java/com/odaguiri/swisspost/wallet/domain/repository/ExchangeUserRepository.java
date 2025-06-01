package com.odaguiri.swisspost.wallet.domain.repository;

import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExchangeUserRepository extends CrudRepository<ExchangeUser, Long> {
    boolean existsByEmail(String email);
    Optional<ExchangeUser> findByEmail(String email);
}
