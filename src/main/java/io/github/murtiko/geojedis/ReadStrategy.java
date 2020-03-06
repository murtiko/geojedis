package io.github.murtiko.geojedis;

import java.util.function.Function;

import redis.clients.jedis.Jedis;

public interface ReadStrategy {

    <T> T read(GeoJedisConfig config, Function<Jedis, T> function);
    
    static ReadStrategy nearest() {
        return new ReadNearest();
    }

}
