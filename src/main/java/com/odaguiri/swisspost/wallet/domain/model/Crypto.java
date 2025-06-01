package com.odaguiri.swisspost.wallet.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "cryptos")
public class Crypto {

    protected Crypto() {
        this.currentPrice = BigDecimal.ZERO;
    }

    public Crypto(String id, String symbol, String name, BigDecimal currentPrice) {
        this();
        this.id = id;
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
    }

    @Id
    private String id;

    @Column(nullable = false, updatable = false)
    private String symbol;

    @Column(nullable = false, updatable = false)
    private String name;

    @Column(precision = 19, scale = 6)
    private BigDecimal currentPrice;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }
}
