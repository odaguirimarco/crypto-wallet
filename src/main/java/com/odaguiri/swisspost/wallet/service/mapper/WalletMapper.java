package com.odaguiri.swisspost.wallet.service.mapper;

import com.odaguiri.swisspost.wallet.domain.model.Asset;
import com.odaguiri.swisspost.wallet.domain.model.Wallet;
import com.odaguiri.swisspost.wallet.service.dto.AssetDto;
import com.odaguiri.swisspost.wallet.service.dto.WalletDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WalletMapper {

    public WalletDto toDto(Wallet wallet) {
        return new WalletDto(
                wallet.getId(),
                wallet.getTotal(),
                mapAssets(wallet.getAssets())
        );
    }

    private List<AssetDto> mapAssets(List<Asset> assets) {
        return assets == null ? List.of() : assets.stream()
                .map(this::toAssetDto)
                .toList();
    }

    private AssetDto toAssetDto(Asset asset) {
        return new AssetDto(
                asset.getSymbol(),
                asset.getQuantity(),
                asset.getPrice(),
                asset.getAssetValue()
        );
    }
}
