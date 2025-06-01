package com.odaguiri.swisspost.wallet.service;

import com.odaguiri.swisspost.wallet.web.dto.SimulationRequest;
import com.odaguiri.swisspost.wallet.web.dto.SimulationResponse;

public interface WalletSimulationService {
    SimulationResponse simulateWalletPerformance(SimulationRequest request);
}
