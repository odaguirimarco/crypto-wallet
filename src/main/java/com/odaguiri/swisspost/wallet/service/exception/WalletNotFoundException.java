package com.odaguiri.swisspost.wallet.service.exception;

public class WalletNotFoundException extends RuntimeException {
    public WalletNotFoundException(Long walletId) {
        super("Wallet not found: " + walletId);
    }

    public String getErrorCode() {
        return "WALLET_NOT_FOUND";
    }
}
