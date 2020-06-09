package io.github.murtiko.geojedis.test;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import io.github.murtiko.geojedis.GeoJedis;
import io.github.murtiko.geojedis.GeoJedisConfig;
import io.github.murtiko.geojedis.ReadStrategy;
import io.github.murtiko.geojedis.WriteStrategy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class GeoJedisGrTests {

    private static EmbeddedRedis localSentinels;
    private static EmbeddedRedis remoteSentinels;

    @BeforeClass
    public static void startEmbeddedRedis() throws Exception {
        localSentinels = new EmbeddedRedis().setReplicationGroup("local");
        remoteSentinels = new EmbeddedRedis().setReplicationGroup("remote");
        localSentinels.start();
        remoteSentinels.start();
    }

    @AfterClass
    public static void stopEmbeddedRedis() {
        localSentinels.stop();
        remoteSentinels.stop();
    }

    @Test
    public void testLocalDownWhileRead() {
        GeoJedisConfig pools = new GeoJedisConfig();

        Pool<Jedis> local = new JedisSentinelPool(localSentinels.getReplicationGroup(),
                localSentinels.getSentinelHosts());
        pools.addLocalPool("local", local);

        Pool<Jedis> remote = new JedisSentinelPool(remoteSentinels.getReplicationGroup(),
                remoteSentinels.getSentinelHosts());
        pools.addRemotePool("remote", remote);

        GeoJedis geoJedis = new GeoJedis(pools, WriteStrategy.allSync(), ReadStrategy.nearest());

        String value = Long.toString(System.nanoTime());

        geoJedis.set("testkey", value);

        localSentinels.stop();

        String read = geoJedis.get("testkey");
        Assert.assertEquals(value, read);
    }

}
