package com.mybot.infrastructure.service;

import java.io.IOException;

public interface SlackService {
    void configure() throws IOException;
    void configMassageOnStartup();


}
