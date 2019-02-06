package com.mybot.infrastructure.properties.nested;

import lombok.Data;

import java.util.List;

@Data
public class SlackConfig {
    String channel;
    String periodicalMessage;
    String successCaseMessage;
    String failureCaseMessage;
    String FailureListener;
    List<String> keywords;
    List<String> users;
    String token;
}
