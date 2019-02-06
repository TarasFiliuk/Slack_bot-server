package com.mybot.infrastructure.domain.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@ToString
public enum StageType {
    STAGE("stage"),
    PREPROD("preprod"),
    PROD("prod");

    private final String value;
}
