package com.odaguiri.swisspost.wallet.web.dto;

public record AuthenticationRequest(
        String username,
        String password
) {}
