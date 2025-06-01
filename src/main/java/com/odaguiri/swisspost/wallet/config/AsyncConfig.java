package com.odaguiri.swisspost.wallet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.VirtualThreadTaskExecutor;

@Configuration
public class AsyncConfig {

    @Bean
    public TaskExecutor applicationTaskExecutor() {
        return new VirtualThreadTaskExecutor();
    }
}
