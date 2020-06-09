package io.github.murtiko.geojedis.resilience4j;

import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

import io.github.murtiko.geojedis.CircuitBreaker;
import io.github.murtiko.geojedis.CircuitBreakerFactory;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;

public class Resilience4JCircuitBreakerFactory implements CircuitBreakerFactory {

    private static Resilience4JCircuitBreakerFactory instance;

    private final CircuitBreakerRegistry registry;
    private final ConcurrentHashMap<String, Resilience4JCircuitBreaker> map;

    private Resilience4JCircuitBreakerFactory(CircuitBreakerRegistry registry) {
        this.registry = registry;
        map = new ConcurrentHashMap<>();
    }

    public static Resilience4JCircuitBreakerFactory getInstance() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50).slowCallRateThreshold(100)
                .slowCallDurationThreshold(Duration.ofSeconds(5000))
                .permittedNumberOfCallsInHalfOpenState(5).minimumNumberOfCalls(5)
                .slidingWindowSize(10).build();
        return getInstance(CircuitBreakerRegistry.of(circuitBreakerConfig));
    }

    public static synchronized Resilience4JCircuitBreakerFactory getInstance(
            CircuitBreakerRegistry registry) {
        if (instance == null) {
            instance = new Resilience4JCircuitBreakerFactory(registry);
        }
        return instance;
    }

    @Override
    public CircuitBreaker get(String key) {
        return map.computeIfAbsent(key,
                k -> new Resilience4JCircuitBreaker(registry.circuitBreaker(k)));
    }

}
