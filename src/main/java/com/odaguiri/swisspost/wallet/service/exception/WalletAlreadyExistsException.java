package com.odaguiri.swisspost.wallet.service.exception;

public class WalletAlreadyExistsException extends RuntimeException {
    public WalletAlreadyExistsException(String email) {
        super("Wallet with email " + email + " already exists");
    }

    public String getErrorCode() {
        return "WALLET_ALREADY_EXISTS";
    }
}
