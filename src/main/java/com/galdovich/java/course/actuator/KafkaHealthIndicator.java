package com.galdovich.java.course.actuator;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) {
        builder.status(getStatus());
    }

    @SneakyThrows
    private Status getStatus() {
        return Status.UP;
    }
}
