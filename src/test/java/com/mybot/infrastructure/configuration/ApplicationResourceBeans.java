package com.mybot.infrastructure.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@ComponentScan("com.mybot.infrastructure.resource")
@EnableWebFlux
public class ApplicationResourceBeans {
}
