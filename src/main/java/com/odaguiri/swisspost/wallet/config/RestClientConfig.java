package com.odaguiri.swisspost.wallet.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.odaguiri.swisspost.wallet.external.CoinCapApiProperties;
import com.odaguiri.swisspost.wallet.security.BearerTokenInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClient;


@Configuration
public class RestClientConfig {

    @Bean
    public RestClient cryptoPriceRestClient(CoinCapApiProperties coinCapApiProperties, RestClient.Builder builder) {
        return builder
                .baseUrl(coinCapApiProperties.getUrl())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .requestInterceptor(new BearerTokenInterceptor(coinCapApiProperties.getKey()))
                .messageConverters(httpMessageConverters -> {
                    httpMessageConverters.add(new MappingJackson2HttpMessageConverter(customObjectMapper()));
                })
                .build();
    }

    private ObjectMapper customObjectMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    }
}
