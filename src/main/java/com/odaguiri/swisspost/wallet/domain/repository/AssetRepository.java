package com.odaguiri.swisspost.wallet.domain.repository;

import com.odaguiri.swisspost.wallet.domain.model.Asset;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AssetRepository extends CrudRepository<Asset, Long> {
    @Modifying
    @Query("UPDATE Asset a SET a.price = ?2, a.assetValue = a.quantity * ?2 WHERE a.symbol = ?1")
    void updateAssetValues(String symbol, BigDecimal newPrice);
}
