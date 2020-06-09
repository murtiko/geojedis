package io.github.murtiko.geojedis;

public interface CircuitBreakerFactory {

    CircuitBreaker get(String key);

}
