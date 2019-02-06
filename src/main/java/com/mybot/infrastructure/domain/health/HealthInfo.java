package com.mybot.infrastructure.domain.health;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybot.infrastructure.domain.health.nested.DiskSpace;
import com.mybot.infrastructure.domain.health.nested.HealthResource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthInfo {
    @JsonInclude(JsonInclude.Include.NON_NULL) private String status;
    @JsonInclude(JsonInclude.Include.NON_NULL) private HealthResource healthResource;
    @JsonInclude(JsonInclude.Include.NON_NULL) private DiskSpace diskSpace;
    @JsonInclude(JsonInclude.Include.NON_NULL) private String dataBase;
}
