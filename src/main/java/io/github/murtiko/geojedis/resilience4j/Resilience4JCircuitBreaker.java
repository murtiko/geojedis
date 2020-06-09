package io.github.murtiko.geojedis.resilience4j;

import java.util.function.Supplier;

import io.github.murtiko.geojedis.CircuitBreaker;

public class Resilience4JCircuitBreaker implements CircuitBreaker {

    private final io.github.resilience4j.circuitbreaker.CircuitBreaker target;

    public Resilience4JCircuitBreaker(io.github.resilience4j.circuitbreaker.CircuitBreaker target) {
        this.target = target;
    }

    @Override
    public <T> T executeSupplier(Supplier<T> supplier) {
        return target.executeSupplier(supplier);
    }

}
