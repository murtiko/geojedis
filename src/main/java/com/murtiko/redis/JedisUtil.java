package com.murtiko.redis;

import java.util.function.Function;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class JedisUtil {

    private JedisUtil() {
    }

    public static <T> T tryExec(Pool<Jedis> pool, Function<Jedis, T> function) {
        try {
            return exec(pool, function);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static <T> T exec(Pool<Jedis> pool, Function<Jedis, T> function) {
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
