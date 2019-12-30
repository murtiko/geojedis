package com.murtiko.redis;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class JedisUtil {

    private static final Logger log = LogManager.getLogger(JedisUtil.class.getName());

    private JedisUtil() {
    }

    public static <T> T tryExec(GeoJedisConfig config, String poolName, Pool<Jedis> pool, Function<Jedis, T> function) {
        try {
            return exec(config, poolName, pool, function);
        } catch (Exception e) {
            log.warn("Failed redis op on " + poolName, e);
            return null;
        }
    }
    
    public static <T> T exec(GeoJedisConfig config, String poolName, Pool<Jedis> pool, Function<Jedis, T> function) {
        CircuitBreakerUtil cbUtil = config.getCircuitBreakerUtil();
        if(cbUtil != null) {
            return config.getCircuitBreakerUtil().get(poolName).executeSupplier(() -> doExec(pool, function));
        }else {
            return doExec(pool, function);
        }
    }

    private static <T> T doExec(Pool<Jedis> pool, Function<Jedis, T> function) {
        Jedis jedis = pool.getResource();
        boolean failed = false;
        try {
            return function.apply(jedis);
        } catch (RuntimeException e) {
            failed = true;
            throw e;
        } finally {
            if (failed) {
                pool.returnBrokenResource(jedis);
            } else {
                pool.returnResource(jedis);
            }
        }
    }
}
