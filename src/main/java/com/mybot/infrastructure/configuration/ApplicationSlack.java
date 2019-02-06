package com.mybot.infrastructure.configuration;

import lombok.RequiredArgsConstructor;
import org.riversun.slacklet.SlackletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ApplicationSlack {
	private final ApplicationProperties applicationProperties;

	@Bean
	public SlackletService slackletService() {
		String token = applicationProperties.getAppConfigs().getSlackConfig().getToken();
		return new SlackletService(token);
	}
}
