package com.rishi.iprangesearch.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

public class CachedRestTemplate extends RestTemplate {

    private final Cache<String, ResponseEntity<String>> responseCache;

    public CachedRestTemplate() {
        this.responseCache = Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(30))
                .maximumSize(100)
                .build();
    }

    @Override
    public <T> ResponseEntity<T> exchange(String url, HttpMethod method, HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables) throws RestClientException {
        String cacheKey = generateCacheKey(url, method);

        ResponseEntity<String> cachedResponse = responseCache.getIfPresent(cacheKey);
        if (cachedResponse != null) {
            return (ResponseEntity<T>) cachedResponse;
        }

        ResponseEntity<T> responseEntity = super.exchange(url, method, requestEntity, responseType, uriVariables);

        if (responseEntity.getStatusCode().is2xxSuccessful())
            responseCache.put(cacheKey, (ResponseEntity<String>) responseEntity);

        return responseEntity;
    }

    private String generateCacheKey(String url, HttpMethod method) {
        return method.name() + "-" + url;
    }
}