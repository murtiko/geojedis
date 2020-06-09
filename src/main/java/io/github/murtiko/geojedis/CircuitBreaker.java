package io.github.murtiko.geojedis;

import java.util.function.Supplier;

public interface CircuitBreaker {

    <T> T executeSupplier(Supplier<T> supplier);
}
