package com.mybot.infrastructure.listener;

import com.mybot.infrastructure.domain.infrastructure.Infrastructure;
import com.mybot.infrastructure.service.ConfigurationService;
import com.mybot.infrastructure.service.InfrastructureService;
import com.mybot.infrastructure.service.SlackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationListener {

	private final ConfigurationService configurationService;
	private final SlackService slackService;
	private final InfrastructureService infrastructureService;

	@EventListener(ApplicationReadyEvent.class)
	public void start() throws IOException {

		// slack
		slackService.configure();

		// infrastructure: configurations
		Infrastructure infrastructure = configurationService.getInfrastructureConfigurations();
		infrastructureService.configure(infrastructure);

		// infrastructure: verify apps
		infrastructureService.verifyApps();
		slackService.configMassageOnStartup();
	}
}
