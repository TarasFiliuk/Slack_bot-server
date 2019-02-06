package com.mybot.infrastructure.service.impl;

import com.mybot.infrastructure.domain.health.HealthInfo;
import com.mybot.infrastructure.domain.infrastructure.App;
import com.mybot.infrastructure.domain.infrastructure.Infrastructure;
import com.mybot.infrastructure.domain.infrastructure.Server;
import com.mybot.infrastructure.domain.infrastructure.Stage;
import com.mybot.infrastructure.service.InfrastructureService;
import com.mybot.infrastructure.utils.ConstantUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InfrastructureServiceImpl implements InfrastructureService {


	private final RestTemplate restTemplate;
	private Infrastructure infrastructure = Infrastructure.builder().build();

	@Override
	public void configure(Infrastructure infrastructure) {
		this.infrastructure = infrastructure;
	}

	@Override
	public void verifyApps() {
		infrastructure.getApps().forEach(app -> {
			LOGGER.info("Started processing app: {} ", app.getName());

			app.getStages().forEach(stage -> {
				LOGGER.info("Started processing app: {}, stage: {}", app.getName(), stage.getType());

				stage.getServers().forEach(server -> {
					LOGGER.info("Started processing app: {}, stage: {}, server: {}", app.getName(), stage.getType(), server.getName());
					Optional<ResponseEntity<HealthInfo>> healthJson = Optional.empty();
					// Health
					String healthURL = server.getIpAddress() + server.getHealthPath();
					try {
						healthJson = Optional.of(restTemplate.getForEntity(healthURL, HealthInfo.class));
					} catch (ResourceAccessException e) {
						LOGGER.info("UNREACHABLE EXCEPTION");
					}
					server.setHealthJson(healthJson);

					LOGGER.info("Completed processing app: {}, stage: {}, server: {}", app.getName(), stage.getType(), server.getName());
				});

				LOGGER.info("Completed processing app: {}, stage: {}", app.getName(), stage.getType());
			});

			LOGGER.info("Completed processing app: {}", app.getName());
		});

	}

	@Override
	public String getInfrastructureAsSlackMessage() {
		LOGGER.debug("Infrastructure: get slack message");
		List<String> draftMatrix = new ArrayList<>();
		draftMatrix.add("```");
		String format = "%-20s %10s %20s %30s %10s";// %20s";
		String header = String.format(format, "AppName", "Stage", "Server Name", "IP Address", "Health");//, "Info");
		draftMatrix.add(header);
		for (App app : infrastructure.getApps()) {
			for (Stage stage : app.getStages()) {
				List<Server> servers = stage.getServers();
				for (int i = 0; i < servers.size(); i++) {
					Server server = servers.get(i);
					LOGGER.info("Processing app: {}, stage: {}, server: {}", app.getName(), stage.getType(), server.getName());
					List<String> draftRow = new ArrayList<>();
					if (i == 0) {
						draftRow.add(app.getName());
						draftRow.add(stage.getType().getValue());
					} else {
						draftRow.add("");
						draftRow.add("");

					}
					draftRow.add(server.getName());
					draftRow.add(server.getIpAddress());
					draftRow.add(server.getHealthAsString());
					String[] row = draftRow.toArray(new String[0]);
					draftMatrix.add(String.format(format, row));
				}
			}
		}

		String message = String.join("\n", draftMatrix);
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(message);
		sb.append("\n");
		System.out.println(ConstantUtils.SEPARATOR);
		System.out.println(sb.toString());
		System.out.println(ConstantUtils.SEPARATOR);
		sb.append("```");
		return sb.toString();
	}

	@Override
	public List<Server> serverListIfFailure() {
		return infrastructure.getApps().stream()
				.map(App::getStages)
				.flatMap(Collection::stream)
				.map(Stage::getServers)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}
}


