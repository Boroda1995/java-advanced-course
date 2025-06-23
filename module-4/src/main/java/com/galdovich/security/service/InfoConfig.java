package com.galdovich.security.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:info.properties")
public class InfoConfig {

    @Value("${application-description}")
    public String applicationDescription;

    @Value("${application-version}")
    public String applicationVersion;

    @Value("${openapi-info}")
    public String openapiInfo;
}
