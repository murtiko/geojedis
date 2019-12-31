package com.murtiko.redis;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class CircuitBreakerFactory {

    private static CircuitBreakerFactory instance;

    private final CircuitBreakerRegistry registry;
    private final ConcurrentHashMap<String, CircuitBreaker> map;

    private CircuitBreakerFactory(CircuitBreakerRegistry registry) {
        this.registry = registry;
        map = new ConcurrentHashMap<>();
    }

    public static CircuitBreakerFactory getInstance() {
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

    public static synchronized CircuitBreakerFactory getInstance(CircuitBreakerRegistry registry) {
        if (instance == null) {
            instance = new CircuitBreakerFactory(registry);
        }
        return instance;
    }

    public CircuitBreaker get(String key) {
        return map.computeIfAbsent(key, k -> registry.circuitBreaker(k));
    }

}
