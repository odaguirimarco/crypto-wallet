package com.odaguiri.swisspost.wallet.web.controller;

import com.odaguiri.swisspost.wallet.service.WalletService;
import com.odaguiri.swisspost.wallet.service.dto.WalletDto;
import com.odaguiri.swisspost.wallet.web.dto.WalletCreateRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(("/api/wallets"))
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<WalletDto> createWallet(@Valid @RequestBody WalletCreateRequest createDto) {
        WalletDto walletDto = walletService.createWallet(createDto);
        return ResponseEntity.created(URI.create("/wallets/" + walletDto.id())).body(walletDto);
    }

    @GetMapping("/{walletId}")
    public ResponseEntity<WalletDto> findWalletById(@Valid @PathVariable Long walletId) {
        return ResponseEntity.ok(walletService.getWallet(walletId));
    }
}
