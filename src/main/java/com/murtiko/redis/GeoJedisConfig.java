package com.murtiko.redis;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class GeoJedisConfig {

    private final ConcurrentHashMap<String, Pool<Jedis>> localPools = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Pool<Jedis>> remotePools = new ConcurrentHashMap<>();
    private CircuitBreakerFactory circuitBreakerFactory = CircuitBreakerFactory.getInstance();

    public void addLocalPool(String name, Pool<Jedis> pool) {
        localPools.put(name, pool);
    }

    public void addRemotePool(String name, Pool<Jedis> pool) {
        remotePools.put(name, pool);
    }

    public Map<String, Pool<Jedis>> getLocalPools() {
        return localPools;
    }

    public Map<String, Pool<Jedis>> getRemotePools() {
        return remotePools;
    }

    public CircuitBreakerFactory getCircuitBreakerFactory() {
        return circuitBreakerFactory;
    }

    public void setCircuitBreakerFactory(CircuitBreakerFactory circuitBreakerFactory) {
        this.circuitBreakerFactory = circuitBreakerFactory;
    }
}
