# geojedis
<p>A simple wrapper to Jedis that replicates operations on multiple data centers on a best-effort basis to provide basic geo-redundancy.</p>

<p>Allows multiple "local" and "remote" pool categories.</p>

<p>redis.clients.jedis.JedisCommands and redis.clients.jedis.BinaryJedisCommands interfaces are implemented.</p>

<p>Different read/write strategies can be selected.</p>

<p>Available Read Strategies are:</p>
<p>ReadNearest : (default) Reads are performed from the first available pool where local pools are attempted prior to remote pools. Pools of same category are attempted in the order they are configured.</p>

<p>Available Write Strategies are:</p>
<p>WriteLocalOnly : (default) Writes are performed only on all local pools, in the order they are configured.</p>
<p>WriteAllSync : Writes are performed on all pools one by one from within the application thread.</p>
<p>WriteNearestSyncRemainingAsync : Writes are performed on the first available pool from within the application thread. Remaining pools are updated in the background using a default cached thread-pool.</p>

<p>It is possible to add custom read/update strategies.</p>

Sample code
------------

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


<p>By default, a resilience4j circuit breaker is used for each pool, with the following default settings:</p>

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(100)
                .slowCallDurationThreshold(Duration.ofSeconds(5000))
                .permittedNumberOfCallsInHalfOpenState(5)
                .minimumNumberOfCalls(5)
                .slidingWindowSize(10)
                .build();

<p>It is possible to customize the circuit breaker, or disable it (by setting to null):</p>
<p>For more info on resilience4j refer to https://resilience4j.readme.io/docs/circuitbreaker</p>

        CircuitBreakerUtil util = CircuitBreakerUtil.getInstance(myCustomCircuitBreakerRegistry);
        GeoJedisConfig config = new GeoJedisConfig().setsetCircuitBreakerUtil(util); // null to disable circuit breaker
        ...
