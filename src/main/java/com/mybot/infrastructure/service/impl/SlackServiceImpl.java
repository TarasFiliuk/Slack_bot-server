package com.mybot.infrastructure.service.impl;

import
		com.mybot.infrastructure.configuration.ApplicationProperties;
import com.mybot.infrastructure.properties.nested.SlackConfig;
import com.mybot.infrastructure.service.InfrastructureService;
import com.mybot.infrastructure.service.SlackService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.riversun.slacklet.Slacklet;
import org.riversun.slacklet.SlackletRequest;
import org.riversun.slacklet.SlackletResponse;
import org.riversun.slacklet.SlackletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Getter
@Setter
@EnableScheduling
public class SlackServiceImpl implements SlackService {
	private static final String FAILURE = "Failure";


	// Infrastructure
	private final InfrastructureService infrastructureService;
	// Slack integration
	private final SlackletService slackletService;
	private final ApplicationProperties applicationProperties;
	private final  ReportedServiceImpl reportedService;
	@Override
	public void configure() throws IOException {
		slackletService.addSlacklet(new Slacklet() {

			@Override
			public void onMentionedMessagePosted(SlackletRequest req, SlackletResponse resp) {
				sendMessageToChatChanel(req, resp);

			}

			@Override
			public void onDirectMessagePosted(SlackletRequest req, SlackletResponse resp) {
				sendMessageToChatChanel(req, resp);
			}
		});

		slackletService.start();
	}

	private void sendMessageToChatChanel(SlackletRequest req, SlackletResponse resp) {
		SlackConfig slackConfig = applicationProperties.getAppConfigs().getSlackConfig();
		String channel = slackConfig.getChannel();
		List<String> users = slackConfig.getUsers();
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);
		String realName = req.getSender().getRealName();
		users.forEach(s -> {
			if (realName.equals(s)) {
				List<String> keywords = slackConfig.getKeywords();
				AtomicBoolean content = new AtomicBoolean(false);
				String reqContent = req.getContent();
				keywords.forEach(aKeywordsforSlack -> {
					if (reqContent.toLowerCase().contains(aKeywordsforSlack.toLowerCase())) {
						reportedService.sendFullReport().run();
						content.set(true);
					}
				});
				if (!content.get()) {
					String collect = keywords.stream()
							.map(String::intern)
							.collect(Collectors.joining("," + " "));
					resp.reply(FAILURE + ":" + " " + reqContent.substring(13) + " - this is no keyword" + "\n Keywords are:" + " " + collect);
				}
				atomicBoolean.set(true);
			}

		});
		if (!atomicBoolean.get()) {
			slackletService.sendMessageTo(channel, "Sorry,but you are not authorized");
		}
	}

	@Override
	public void configMassageOnStartup() {
		SlackConfig slackConfig = applicationProperties.getAppConfigs().getSlackConfig();
		String periodicalMessage = slackConfig.getPeriodicalMessage();
		String failureListener = slackConfig.getFailureListener();
		ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setPoolSize(10);
		threadPoolTaskScheduler.initialize();
		threadPoolTaskScheduler.schedule(reportedService.postedMassageRunnable(), new CronTrigger(periodicalMessage, TimeZone.getTimeZone("Europe/Kiev")));
		threadPoolTaskScheduler.schedule(reportedService.listeningFailure(), new CronTrigger(failureListener));
	}
}