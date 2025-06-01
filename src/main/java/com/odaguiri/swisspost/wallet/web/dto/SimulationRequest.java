package com.odaguiri.swisspost.wallet.web.dto;

import java.time.LocalDate;
import java.util.List;

public record SimulationRequest(
        List<AssetInput> assets,
        LocalDate simulationDate
) {}
