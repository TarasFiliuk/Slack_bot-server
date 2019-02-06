package com.mybot.infrastructure.configuration;

import com.mybot.infrastructure.utils.EnvironmentUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

@Configuration
public class ApplicationServiceMockBeans {
    // Utils
    @Bean
    public EnvironmentUtils environmentUtils() {
        return mock(EnvironmentUtils.class);
    }
}
