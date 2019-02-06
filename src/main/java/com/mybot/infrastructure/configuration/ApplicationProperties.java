package com.mybot.infrastructure.configuration;

import com.mybot.infrastructure.properties.AppConfigs;
import com.mybot.infrastructure.properties.Logging;
import com.mybot.infrastructure.properties.Server;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;

@ConfigurationProperties
@Data
public class ApplicationProperties implements PriorityOrdered {
	private Logging logging;
	private Server server;
	private AppConfigs appConfigs;

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}
}
