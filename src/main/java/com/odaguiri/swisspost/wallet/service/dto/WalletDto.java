package com.odaguiri.swisspost.wallet.service.dto;

import java.math.BigDecimal;
import java.util.List;

public record WalletDto(
        Long id,
        BigDecimal total,
        List<AssetDto> assets
) {}
