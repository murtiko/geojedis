package io.github.murtiko.geojedis;

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
        	log.debug("Writing on local {}", p.getKey());
            resp = doWrite(config, function, resp, p);
        }
        
        for(Entry<String, Pool<Jedis>> p : config.getRemotePools().entrySet()) {
        	log.debug("Writing on remote {}", p.getKey());
            resp = doWrite(config, function, resp, p);
        }

        return resp;
    }

    private <T> T doWrite(GeoJedisConfig config, Function<Jedis, T> function, T resp, Entry<String, Pool<Jedis>> p) {
        try {
            if(resp == null) {
                resp = JedisUtil.exec(config, p.getKey(), p.getValue(), function);
            } else {
                executor.submit(() -> JedisUtil.tryExec(config, p.getKey(), p.getValue(), function));
            }
        } catch (Exception e) {
        	log.warn("Failed redis op on " + p.getKey(), e);
        }
        return resp;
    }  
    
}
