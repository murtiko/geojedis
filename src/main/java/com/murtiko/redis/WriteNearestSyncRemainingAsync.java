package com.murtiko.redis;

import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.util.Pool;

public class WriteNearestSyncRemainingAsync implements WriteStrategy {

    private static final Logger log = LogManager.getLogger(WriteNearestSyncRemainingAsync.class.getName());

    private ExecutorService executor = Executors.newCachedThreadPool();
    
    @Override
    public <T> T write(GeoJedisConfig config, Function<Jedis, T> function) {
        T resp = null;
        
        for(Entry<String, Pool<Jedis>> p : config.getLocalPools().entrySet()) {
            resp = doWrite(function, resp, p);
        }
        
        for(Entry<String, Pool<Jedis>> p : config.getRemotePools().entrySet()) {
            resp = doWrite(function, resp, p);
        }

        return resp;
    }

    private <T> T doWrite(Function<Jedis, T> function, T resp, Entry<String, Pool<Jedis>> p) {
        try {
            if(resp == null) {
                resp = JedisUtil.exec(p.getValue(), function);
            } else {
                executor.submit(() -> JedisUtil.exec(p.getValue(), function));
            }
        } catch (Exception e) {
            log.warn("Failed redis op on " + p.getKey(), e);
        }
        return resp;
    }  
    
}
