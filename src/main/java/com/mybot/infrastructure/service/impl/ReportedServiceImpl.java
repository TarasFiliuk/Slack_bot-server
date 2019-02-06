package com.mybot.infrastructure.service.impl;

import com.mybot.infrastructure.configuration.ApplicationProperties;
import com.mybot.infrastructure.domain.infrastructure.Server;
import com.mybot.infrastructure.service.InfrastructureService;
import com.mybot.infrastructure.utils.ConstantUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.riversun.slacklet.SlackletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.StringJoiner;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Setter
public class ReportedServiceImpl {
	// Infrastructure
	private final InfrastructureService infrastructureService;
	// Slack integration
	private final SlackletService slackletService;
	private final ApplicationProperties applicationProperties;

	Runnable postedMassageRunnable() {
		String channel = applicationProperties.getAppConfigs().getSlackConfig().getChannel();
		String successCaseMessage = applicationProperties.getAppConfigs().getSlackConfig().getSuccessCaseMessage();
		return () -> {
			infrastructureService.verifyApps();
			String infrastructureAsSlackMessage = infrastructureService.getInfrastructureAsSlackMessage();
			if (!infrastructureAsSlackMessage.contains(ConstantUtils.FAILURE)) {
				slackletService.sendMessageTo(channel, successCaseMessage);
			} else {
				listeningFailure().run();
			}
		};
	}

	Runnable sendFullReport() {
		return () -> {
			infrastructureService.verifyApps();
			String channel = applicationProperties.getAppConfigs().getSlackConfig().getChannel();
			String slackMessage = infrastructureService.getInfrastructureAsSlackMessage();
			slackletService.sendMessageTo(channel, slackMessage);
		};
	}


	Runnable listeningFailure() {
		String channel = applicationProperties.getAppConfigs().getSlackConfig().getChannel();
		String failureCaseMessage = applicationProperties.getAppConfigs().getSlackConfig().getFailureCaseMessage();
		return () -> {
			infrastructureService.verifyApps();
			String infrastructureAsSlackMessage = infrastructureService.getInfrastructureAsSlackMessage();
			if (infrastructureAsSlackMessage.contains(ConstantUtils.FAILURE)) {
				StringJoiner joiner = new StringJoiner("\n");
				infrastructureService.serverListIfFailure().stream().filter(server -> !server.getHealthJson().isPresent())
						.map(Server::getName)
						.forEach(joiner::add);
				String failureServerName = joiner.toString();
				slackletService.sendMessageTo(channel, failureCaseMessage + " \n" + failureServerName);

			}
		};
	}
}
