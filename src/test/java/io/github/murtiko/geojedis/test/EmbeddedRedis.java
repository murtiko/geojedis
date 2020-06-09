package io.github.murtiko.geojedis.test;

import java.util.Set;

import redis.embedded.RedisCluster;
import redis.embedded.RedisSentinel;
import redis.embedded.RedisServer;
import redis.embedded.util.JedisUtil;

public class EmbeddedRedis {

    private String replicationGroup = "mymaster";
    private RedisCluster cluster;
    private Set<String> sentinelHosts;

    public void start() throws Exception {

        cluster = RedisCluster.builder().ephemeral().sentinelCount(3).quorumSize(2)
                .withServerBuilder(RedisServer.builder().setting("maxmemory 32")
                        .setting("maxheap 32M").setting("maxclients 1024"))
                .withSentinelBuilder(
                        RedisSentinel.builder().setting("maxmemory 32").setting("maxheap 32M"))
                .sentinelCount(3).quorumSize(2).replicationGroup(replicationGroup, 1).build();

        cluster.start();

        // retrieve ports on which sentinels have been started, using a simple
        // Jedis utility class
        sentinelHosts = JedisUtil.sentinelHosts(cluster);
    }

    public void stop() {
        if (cluster != null) {
            cluster.stop();
        }
    }

    public String getReplicationGroup() {
        return replicationGroup;
    }

    public EmbeddedRedis setReplicationGroup(String replicationGroup) {
        this.replicationGroup = replicationGroup;
        return this;
    }

    public Set<String> getSentinelHosts() {
        return sentinelHosts;
    }

}
