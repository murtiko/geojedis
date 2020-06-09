# geojedis
<p>A simple wrapper to Jedis that replicates operations on multiple Sentinel pools (potentially residing on different data centers) on a best-effort basis to provide basic geo-redundancy features.</p>

<p>Allows "local" and "remote" pool categories, where multiple pools can be registered with each category.</p>

<p>redis.clients.jedis.JedisCommands and redis.clients.jedis.BinaryJedisCommands interfaces are implemented.</p>

<p>Different read/write strategies can be selected.</p>

<p>Available Read Strategies are:</p>
<p>ReadNearest : (default) Reads are performed from the first available pool where local pools are attempted prior to remote pools. Pools of same category are attempted in the order they are configured.</p>

<p>Available Write Strategies are:</p>
<p>WriteLocalOnly : (default) Writes are performed only on all local pools, in the order they are configured.</p>
<p>WriteAllSync : Writes are performed on all pools one by one from within the application thread, where local pools are attempted prior to remote pools.</p>
<p>WriteNearestSyncRemainingAsync : Writes are performed on the first available pool from within the application thread. Remaining pools are updated in the background using a default cached thread-pool.</p>

<p>It is possible to add custom read/update strategies.</p>

Sample code
------------

        GeoJedisConfig pools = new GeoJedisConfig();
        
        Pool<Jedis> local = new JedisSentinelPool("mymaster", 
                new HashSet<>(Arrays.asList(new String[] {"localsentinel01:26379",
                    "localsentinel02:26379", "localsentinel03:26379"})));
        pools.addLocalPool("local", local);
        
        Pool<Jedis> remote = new JedisSentinelPool("mymaster", 
                new HashSet<>(Arrays.asList(new String[] {"remotesentinel01:26379",
                    "remotesentinel02:26379", "remotesentinel03:26379"})));
        pools.addRemotePool("remote", remote);
        
        GeoJedis geoJedis = new GeoJedis(pools);
        
        geoJedis.set("testkey", "testvalue");
        System.out.println("set testkey=testvalue");
        
        String val = geoJedis.get("testkey");
        System.out.println("queried testkey=" + val);

<p>It is possible to wrap pool operations with circuit breakers. By default the circuit breaker functionality is disabled. It is possible to use a default resilience4j based one, or configure an external "circuit-breaker factory" impl.</p>

        // disables circuit-breaker
        pools.setCircuitBreakerFactory(null);
        
        // enables the default resilience4j based circuit-breaker (with default settings)
        pools.setCircuitBreakerFactory(Resilience4JCircuitBreakerFactory.getInstance());
        
        // enables resilience4j based circuit-breaker (with custom registry)
        CircuitBreakerRegistry customRegistry = CircuitBreakerRegistry.of(customConfig);
        pools.setCircuitBreakerFactory(Resilience4JCircuitBreakerFactory.getInstance(customRegistry));

        // or use a custom circuit-breaker..

<p>The default circuit breaker is configured with the following settings:</p>

        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)
                .slowCallRateThreshold(100)
                .slowCallDurationThreshold(Duration.ofSeconds(5000))
                .permittedNumberOfCallsInHalfOpenState(5)
                .minimumNumberOfCalls(5)
                .slidingWindowSize(10)
                .build();

<p>For more info on resilience4j refer to https://resilience4j.readme.io/docs/circuitbreaker</p>

