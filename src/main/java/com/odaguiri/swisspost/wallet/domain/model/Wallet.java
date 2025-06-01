package com.odaguiri.swisspost.wallet.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallets", indexes = @Index(name = "idx_wallet_id", columnList = "id"))
public class Wallet {

    protected Wallet() {
        this.total = BigDecimal.ZERO;
        this.assets = new ArrayList<>();
    }

    public Wallet(ExchangeUser exchangeUser) {
        this();
        setUser(exchangeUser);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(precision = 19, scale = 6)
    private BigDecimal total;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "exchange_user_id", nullable = false)
    private ExchangeUser exchangeUser;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Asset> assets;

    public void upsertAsset(Asset newAsset) {
        this.assets.stream()
                .filter(asset -> asset.getSymbol().equals(newAsset.getSymbol()))
                .findFirst()
                .ifPresentOrElse(
                        existingAsset -> existingAsset.updateFrom(newAsset),
                        () -> this.addNewAsset(newAsset));
        calculateTotal();
    }

    private void calculateTotal() {
        this.total = assets.stream()
                .map(Asset::getAssetValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void addNewAsset(Asset newAsset) {
        newAsset.setWallet(this);
        this.assets.add(newAsset);
    }

    public Long getId() {
        return id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public ExchangeUser getUser() {
        return exchangeUser;
    }

    public List<Asset> getAssets() {
        return assets;
    }

    private void setUser(ExchangeUser exchangeUser) {
        this.exchangeUser = exchangeUser;
        exchangeUser.setWallet(this);
    }
}
