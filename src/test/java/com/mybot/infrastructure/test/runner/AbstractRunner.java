package com.mybot.infrastructure.test.runner;

import com.mybot.infrastructure.properties.AppConfigs;
import com.mybot.infrastructure.properties.nested.Cors;

public abstract class AbstractRunner {

    protected AppConfigs appConfigs() {
        AppConfigs appConfigs = new AppConfigs();
        // CORS
        Cors cors = new Cors();
        cors.setEnabled(true);
        appConfigs.setCors(cors);
        return appConfigs;
    }
}
