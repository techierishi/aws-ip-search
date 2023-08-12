package com.rishi.iprangesearch.config;

import com.rishi.iprangesearch.util.CachedRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new CachedRestTemplate();
    }
}
