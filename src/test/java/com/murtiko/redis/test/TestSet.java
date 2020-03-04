package com.murtiko.redis.test;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.murtiko.redis.GeoJedis;
import com.murtiko.redis.GeoJedisConfig;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class TestSet {

	@Test
	@Ignore
	public void test() {
		GeoJedisConfig pools = new GeoJedisConfig();
	    
	    Pool<Jedis> local = new JedisSentinelPool("mymaster", 
	            new HashSet<>(Arrays.asList(new String[] {"localsentinel01:26379", "localsentinel02:26379", "localsentinel03:26379"})));
	    pools.addLocalPool("local", local);
	    
	    Pool<Jedis> remote = new JedisSentinelPool("mymaster", 
	            new HashSet<>(Arrays.asList(new String[] {"remotesentinel01:26379", "remotesentinel02:26379", "remotesentinel03:26379"})));
	    pools.addRemotePool("remote", remote);
	    
	    GeoJedis geoJedis = new GeoJedis(pools);
	    
	    geoJedis.set("testkey", "testvalue");
	    System.out.println("set testkey=testvalue");
	    
	    String val = geoJedis.get("testkey");
	    System.out.println("queried testkey=" + val);
	    
	    Assert.assertEquals("testvalue", val);
	}
}
