package io.github.murtiko.geojedis.test;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import io.github.murtiko.geojedis.GeoJedis;
import io.github.murtiko.geojedis.GeoJedisConfig;
import io.github.murtiko.geojedis.ReadStrategy;
import io.github.murtiko.geojedis.WriteStrategy;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class TestSet {

	public static void main(String[] args) {
		GeoJedisConfig pools = new GeoJedisConfig();
	    
	    Pool<Jedis> local = new JedisSentinelPool("rdjd_vcp", 
	            new HashSet<>(Arrays.asList(new String[] {"jdvcpredis01-lan:26379", "jdvcpredis02-lan:26379", "jdvcprep02-lan:26379"})));
	    pools.addLocalPool("local", local);
	    
	    Pool<Jedis> remote = new JedisSentinelPool("rdry_vcp", 
	            new HashSet<>(Arrays.asList(new String[] {"ryvcpredis01:26379", "ryvcpredis02:26379", "ryvcprep02:26379"})));
	    pools.addRemotePool("remote", remote);
	    
	    pools.setCircuitBreakerFactory(null);

	    GeoJedis geoJedis = new GeoJedis(pools, WriteStrategy.allSync(), ReadStrategy.nearest());  
	    
	    geoJedis.set("testkey", "testvalue");
	    System.out.println("set testkey=testvalue");
	    
	    String val = geoJedis.get("testkey");
	    System.out.println("queried testkey=" + val);
	    
	    Assert.assertEquals("testvalue", val);
	    
	}
	
}
