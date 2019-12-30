package com.murtiko.redis;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import redis.clients.jedis.Jedis;

public class WriteAllSync implements WriteStrategy {

    @Override
    public <T> T write(GeoJedisConfig config, Function<Jedis, T> function) {
        final List<T> resp = new LinkedList<>();
        config.getLocalPools().forEach((k, v) -> resp.add(JedisUtil.tryExec(config, k, v, function)));
        config.getRemotePools().forEach((k, v) -> JedisUtil.tryExec(config, k, v, function));
        return resp.isEmpty() ? null : resp.get(0);
    }

}
