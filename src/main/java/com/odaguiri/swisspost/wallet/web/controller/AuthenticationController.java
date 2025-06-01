package com.odaguiri.swisspost.wallet.web.controller;

import com.odaguiri.swisspost.wallet.domain.model.ExchangeUser;
import com.odaguiri.swisspost.wallet.security.JwtTokenUtil;
import com.odaguiri.swisspost.wallet.service.impl.ExchangeUserServiceImpl;
import com.odaguiri.swisspost.wallet.web.dto.AuthenticationRequest;
import com.odaguiri.swisspost.wallet.web.dto.AuthenticationResponse;
import com.odaguiri.swisspost.wallet.web.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final ExchangeUserServiceImpl exchangeUserService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(ExchangeUserServiceImpl exchangeUserService,
                                    AuthenticationManager authenticationManager,
                                    JwtTokenUtil jwtTokenUtil,
                                    PasswordEncoder passwordEncoder) {
        this.exchangeUserService = exchangeUserService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (exchangeUserService.existsByEmail(request.username())) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        ExchangeUser newExchangeUser = new ExchangeUser(request.username(), encodedPassword);
        exchangeUserService.create(newExchangeUser);

        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.username(),
                        authenticationRequest.password()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
