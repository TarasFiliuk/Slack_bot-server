package com.mybot.infrastructure.domain.infrastructure;

import com.mybot.infrastructure.domain.health.HealthInfo;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


@Builder
@Data
public class Server {
    // Spring Boot: V1
    public static final String SPRING_BOOT_V1_HEALTH = "/health";
    public static final String SPRING_BOOT_V1_INFO = "/info";

    // Spring Boot: V2
    public static final String SPRING_BOOT_V2_HEALTH = "/actuator/health";
    public static final String SPRING_BOOT_V2_INFO = "/actuator/info";

    // Given
    private String name;
    private String ipAddress;
    private String healthPath;
    private String infoPath;

    // Outputs
    private Optional<ResponseEntity<HealthInfo>> healthJson;

    public String getHealthAsString() {
        if (healthJson.isPresent()) {
            return "OK";
        } else {
            return "Failure";
        }
    }
}
