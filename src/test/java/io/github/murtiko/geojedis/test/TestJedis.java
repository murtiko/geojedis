package io.github.murtiko.geojedis.test;

import java.util.Arrays;
import java.util.HashSet;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

public class TestJedis {
	
	public static void main(String[] args) {
	    Pool<Jedis> local = new JedisSentinelPool(args[0], 
	            new HashSet<>(Arrays.asList(new String[] {args[1], args[2], args[3]})));
	    Jedis jedis = local.getResource();
	   
	    jedis.set("testkey", "testvalue");
	    System.out.println("WITH JEDIS : set testkey=testvalue");
	    
	    String val = jedis.get("testkey");
	    System.out.println("WITH JEDIS : queried testkey=" + val);

	}

}
