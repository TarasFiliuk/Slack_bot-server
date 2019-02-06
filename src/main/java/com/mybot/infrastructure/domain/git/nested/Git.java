package com.mybot.infrastructure.domain.git.nested;

import lombok.Data;

@Data
public class Git {
    private Commit commit;
    private String branch;
}
