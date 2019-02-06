package com.mybot.infrastructure.service;

import com.mybot.infrastructure.domain.infrastructure.Infrastructure;
import com.mybot.infrastructure.domain.infrastructure.Server;

import java.util.List;

public interface InfrastructureService {
    void configure(Infrastructure infrastructure);
    void verifyApps();
    String getInfrastructureAsSlackMessage();
    List<Server> serverListIfFailure();
}
