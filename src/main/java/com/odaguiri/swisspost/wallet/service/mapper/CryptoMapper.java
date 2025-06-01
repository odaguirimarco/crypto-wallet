package com.odaguiri.swisspost.wallet.service.mapper;

import com.odaguiri.swisspost.wallet.domain.model.Crypto;
import com.odaguiri.swisspost.wallet.service.dto.CryptoDto;
import org.springframework.stereotype.Component;

@Component
public class CryptoMapper {

    public CryptoDto toDto(Crypto crypto) {
        return new CryptoDto(
                crypto.getId(),
                crypto.getSymbol(),
                crypto.getName(),
                crypto.getCurrentPrice()
        );
    }

    public Crypto toEntity(CryptoDto dto) {
        return new Crypto(dto.id(), dto.symbol(), dto.name(), dto.currentPrice());
    }
}
