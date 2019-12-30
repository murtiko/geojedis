package com.murtiko.redis;

import java.util.Map.Entry;
import java.util.function.Function;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class ReadNearest implements ReadStrategy {

    @Override
    public <T> T read(GeoJedisConfig config, Function<Jedis, T> function) {
        for(Entry<String, Pool<Jedis>> p : config.getLocalPools().entrySet()) {
            try {
                return JedisUtil.exec(p.getValue(), function);
            } catch (Exception e) {
                e.printStackTrace();
                // Tracer.trace(Level.WARNING, "Failed redis op on {0} : {1}. Will try next one..", p.getKey(), e);
            }
        }
        for(Entry<String, Pool<Jedis>> p : config.getRemotePools().entrySet()) {
            try {
                return JedisUtil.exec(p.getValue(), function);
            } catch (Exception e) {
                e.printStackTrace();
                // Tracer.trace(Level.WARNING, "Failed redis op on {0} : {1}. Will try next one..", p.getKey(), e);
            }
        }
        return null;
    }

}
