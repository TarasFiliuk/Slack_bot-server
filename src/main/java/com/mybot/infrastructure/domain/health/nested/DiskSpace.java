package com.mybot.infrastructure.domain.health.nested;

import lombok.Data;

@Data
public class DiskSpace {
    private String status;
    private long total;
    private long free;
    private long threshold;
}
