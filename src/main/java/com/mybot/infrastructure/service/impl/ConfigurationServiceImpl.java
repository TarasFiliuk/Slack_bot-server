package com.mybot.infrastructure.service.impl;

import com.mybot.infrastructure.domain.infrastructure.*;
import com.mybot.infrastructure.service.ConfigurationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.mybot.infrastructure.domain.infrastructure.Server.*;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;

@Slf4j
@Service
public class ConfigurationServiceImpl implements ConfigurationService {

	@Override
	public Infrastructure getInfrastructureConfigurations() {

		return Infrastructure.builder()
				.apps(
						asList(
								getMusiciansFirst(),
								getTestServer()
						)
				)
				.build();
	}

	private App getTestServer() {
		return App.builder()
				.name("Development Server")
				.stages(
						singletonList(
								Stage.builder()
										.type(StageType.PROD)
										.servers(
												singletonList(
														Server.builder()
																.name("development-server")
																.ipAddress("http://localhost:8999")
																.healthPath(SPRING_BOOT_V2_HEALTH)
																.infoPath(SPRING_BOOT_V2_INFO)
																.build()
												)
										)
										.build()
						)
				)
				.build();
	}

	// ================================================================================================================
	// Private Methods: MusiciansFirst
	// ================================================================================================================
	private App getMusiciansFirst() {
		// Musician First
		return App.builder()
				.name("Musician First")
				.stages(
						singletonList(
								Stage.builder()
										.type(StageType.PROD)
										.servers(
												singletonList(
														Server.builder()
																.name("mockuphone.bot")
																.ipAddress("http://46.101.17.215:8484")
																.healthPath(SPRING_BOOT_V2_HEALTH)
																.infoPath(SPRING_BOOT_V2_INFO)
																.build()
												)
										)
										.build()

						)
				)
				.build();

	}


}
