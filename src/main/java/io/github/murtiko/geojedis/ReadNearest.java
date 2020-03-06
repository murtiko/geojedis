package io.github.murtiko.geojedis;

import java.util.Map.Entry;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class ReadNearest implements ReadStrategy {

    private static final Logger log = LogManager.getLogger(ReadNearest.class.getName());

    @Override
    public <T> T read(GeoJedisConfig config, Function<Jedis, T> function) {
        for(Entry<String, Pool<Jedis>> p : config.getLocalPools().entrySet()) {
            try {
                return JedisUtil.exec(config, p.getKey(), p.getValue(), function);
            } catch (Exception e) {
                log.warn("Failed redis op on " + p.getKey(), e);
            }
        }
        for(Entry<String, Pool<Jedis>> p : config.getRemotePools().entrySet()) {
            try {
                return JedisUtil.exec(config, p.getKey(), p.getValue(), function);
            } catch (Exception e) {
                log.warn("Failed redis op on " + p.getKey(), e);
            }
        }
        return null;
    }

}
