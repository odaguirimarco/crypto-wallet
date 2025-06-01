package com.odaguiri.swisspost.wallet.web.controller;

import com.odaguiri.swisspost.wallet.service.WalletSimulationService;
import com.odaguiri.swisspost.wallet.web.dto.SimulationRequest;
import com.odaguiri.swisspost.wallet.web.dto.SimulationResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simulations")
public class SimulationController {

    private final WalletSimulationService walletSimulationService;

    public SimulationController(WalletSimulationService walletSimulationService) {
        this.walletSimulationService = walletSimulationService;
    }

    @PostMapping
    public ResponseEntity<SimulationResponse> simulateWalletPerformance(@RequestBody SimulationRequest request) {
        return ResponseEntity.ok(walletSimulationService.simulateWalletPerformance(request));
    }
}
