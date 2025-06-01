package com.odaguiri.swisspost.wallet.domain.model;

import jakarta.persistence.*;

@Entity
@Table(name = "exchange_users")
public class ExchangeUser {

    protected ExchangeUser() {
    }

    public ExchangeUser(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToOne(mappedBy = "exchangeUser", cascade = CascadeType.ALL)
    private Wallet wallet;

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
