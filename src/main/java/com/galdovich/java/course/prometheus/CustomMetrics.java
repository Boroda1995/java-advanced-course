package com.galdovich.java.course.prometheus;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Component;

@Component
public class CustomMetrics {

    private final Counter customCounter;

    public CustomMetrics(MeterRegistry meterRegistry) {
        this.customCounter = Counter.builder("product_request_count")
            .description("Request count of api/v1/products api that can load the DB")
            .register(meterRegistry);
    }

    public void incrementCustomCounter() {
        customCounter.increment();
    }
}
