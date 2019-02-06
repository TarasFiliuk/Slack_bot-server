package com.mybot.infrastructure.domain.infrastructure;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Stage {
    private StageType type;
    private List<Server> servers;
}
