package com.odaguiri.swisspost.wallet.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record WalletCreateRequest(
        @Email @NotBlank String email
) {}
