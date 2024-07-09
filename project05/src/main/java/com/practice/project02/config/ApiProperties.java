package com.practice.project02.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "api.security")
public class ApiProperties {

    private String[] ignored;
}
