package com.mybot.infrastructure.domain.infrastructure;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class App {
    private String name;
    private List<Stage> stages;
}
