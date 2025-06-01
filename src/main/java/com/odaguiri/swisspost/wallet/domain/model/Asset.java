package com.odaguiri.swisspost.wallet.domain.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "assets")
public class Asset {

    protected Asset() {
        this.quantity = BigDecimal.ZERO;
        this.price = BigDecimal.ZERO;
        this.assetValue = BigDecimal.ZERO;
    }

    public Asset(String symbol, BigDecimal quantity, BigDecimal price) {
        this();
        this.symbol = symbol;
        this.quantity = quantity;
        this.price = price;
        calculateAssetValue();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String symbol;

    @Column(precision = 38, scale = 18)
    private BigDecimal quantity;

    @Column(precision = 19, scale = 6)
    private BigDecimal price;

    @Column(precision = 19, scale = 6)
    private BigDecimal assetValue;

    @ManyToOne
    @JoinColumn(name = "wallet_id", updatable = false)
    private Wallet wallet;

    public void updateFrom(Asset newAsset) {
        this.quantity = newAsset.quantity;
        this.price = newAsset.price;
        calculateAssetValue();
    }

    private void calculateAssetValue() {
        this.assetValue = this.quantity.multiply(this.price);
    }

    public Long getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getAssetValue() {
        return assetValue;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}
