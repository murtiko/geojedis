package com.murtiko.redis;

import java.util.function.Function;

import redis.clients.jedis.Jedis;

public interface WriteStrategy {

    <T> T write(GeoJedisConfig config, Function<Jedis, T> function);
    
    static WriteStrategy nearestSyncRemainingAsync() {
        return new WriteNearestSyncRemainingAsync();
    }
    
    static WriteStrategy allSync() {
        return new WriteAllSync();
    }

}
