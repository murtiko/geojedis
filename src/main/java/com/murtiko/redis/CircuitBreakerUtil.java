package com.murtiko.redis;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class CircuitBreakerUtil {

    private static CircuitBreakerUtil instance;

    private final CircuitBreakerRegistry registry;
    private final ConcurrentHashMap<String, CircuitBreaker> map;

    private CircuitBreakerUtil(CircuitBreakerRegistry registry) {
        this.registry = registry;
        map = new ConcurrentHashMap<>();
    }

    public static CircuitBreakerUtil getInstance() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(100)
                .slowCallDurationThreshold(Duration.ofSeconds(5000))
                .permittedNumberOfCallsInHalfOpenState(5)
                .minimumNumberOfCalls(5)
                .slidingWindowSize(10)
                .build();
                
        return getInstance(CircuitBreakerRegistry.of(circuitBreakerConfig));
    }

    public static synchronized CircuitBreakerUtil getInstance(CircuitBreakerRegistry registry) {
        if (instance == null) {
            instance = new CircuitBreakerUtil(registry);
        }
        return instance;
    }

    public CircuitBreaker get(String key) {
        return map.computeIfAbsent(key, k -> registry.circuitBreaker(k));
    }

}