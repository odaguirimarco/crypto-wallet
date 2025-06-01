package com.odaguiri.swisspost.wallet.domain.repository;

import com.odaguiri.swisspost.wallet.domain.model.Asset;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {
}
