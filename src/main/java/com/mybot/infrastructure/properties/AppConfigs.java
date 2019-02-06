package com.mybot.infrastructure.properties;

import com.mybot.infrastructure.properties.nested.Actuator;
import com.mybot.infrastructure.properties.nested.Cors;
import com.mybot.infrastructure.properties.nested.SlackConfig;
import lombok.Data;

@Data
public class AppConfigs {
    private Cors cors;
    private Actuator actuator;
    private SlackConfig slackConfig;
}
