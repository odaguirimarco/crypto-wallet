package com.odaguiri.swisspost.wallet.web.controller;

import com.odaguiri.swisspost.wallet.service.WalletService;
import com.odaguiri.swisspost.wallet.service.dto.WalletDto;
import com.odaguiri.swisspost.wallet.web.dto.AssetAdditionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final WalletService walletService;

    public AssetController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PatchMapping("/{walletId}")
    public ResponseEntity<WalletDto> addAsset(@PathVariable Long walletId,
                                              @RequestBody AssetAdditionRequest request) {
        WalletDto walletDto = walletService.addOrUpdateAsset(
                walletId,
                request.symbol(),
                request.amount(),
                request.price());
        return ResponseEntity.ok(walletDto);
    }
}
