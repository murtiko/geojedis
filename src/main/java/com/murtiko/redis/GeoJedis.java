package com.murtiko.redis;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;

import redis.clients.jedis.BinaryClient.LIST_POSITION;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;
import redis.clients.util.Pool;

public class GeoJedis implements redis.clients.jedis.JedisCommands, redis.clients.jedis.BinaryJedisCommands,
        java.io.Closeable {

    private final GeoJedisConfig config;
    private final WriteStrategy writeStrategy;
    private final ReadStrategy readStrategy;
    
    public GeoJedis(GeoJedisConfig config) {
        this(config, WriteStrategy.nearestSyncRemainingAsync(), ReadStrategy.nearest());
    }
    
    public GeoJedis(GeoJedisConfig config, WriteStrategy writeStrategy, ReadStrategy readStrategy) {
        this.config = config;
        this.writeStrategy = writeStrategy;
        this.readStrategy = readStrategy;
    }
    
    // ----
    
    private <T> T write(Function<Jedis, T> function) {
        return writeStrategy.write(config, function);
    }

    private <T> T read(Function<Jedis, T> function) {
        return readStrategy.read(config, function);
    }

    // ---- java.io.Closeable
    
    @Override
    public void close() throws IOException {
        config.getLocalPools().forEach((k, v) -> tryClose(v));
        config.getRemotePools().forEach((k, v) -> tryClose(v));
    }

    private void tryClose(Pool<Jedis> v) {
        try {
            v.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    

    // ---- redis.clients.jedis.JedisCommands
    
    @Override
    public Long append(String arg0, String arg1) {
        return write(j -> j.append(arg0, arg1));
    }
        
    @Override
    public Long bitcount(String arg0) {
        return read(j -> j.bitcount(arg0));
    }

    @Override
    public Long bitcount(String arg0, long arg1, long arg2) {
        return read(j -> j.bitcount(arg0, arg1, arg2));
    }

    @Override
    public List<String> blpop(String arg0) {
        return write(j -> j.blpop(arg0));
    }

    @Override
    public List<String> blpop(int arg0, String arg1) {
        return write(j -> j.blpop(arg0, arg1));
    }

    @Override
    public List<String> brpop(String arg0) {
        return write(j -> j.brpop(arg0));
    }

    @Override
    public List<String> brpop(int arg0, String arg1) {
        return write(j -> j.brpop(arg0, arg1));
    }

    @Override
    public Long decr(String arg0) {
        return write(j -> j.decr(arg0));
    }

    @Override
    public Long decrBy(String arg0, long arg1) {
        return write(j -> j.decrBy(arg0, arg1));
    }

    @Override
    public Long del(String arg0) {
        return write(j -> j.del(arg0));
    }

    @Override
    public String echo(String arg0) {
        return read(j -> j.echo(arg0));
    }

    @Override
    public Boolean exists(String arg0) {
        return write(j -> j.exists(arg0));
    }

    @Override
    public Long expire(String arg0, int arg1) {
        return write(j -> j.expire(arg0, arg1));
    }

    @Override
    public Long expireAt(String arg0, long arg1) {
        return write(j -> j.expireAt(arg0, arg1));
    }

    @Override
    public String get(String arg0) {
        return read(j -> j.get(arg0));
    }

    @Override
    public String getSet(String arg0, String arg1) {
        return write(j -> j.getSet(arg0, arg1));
    }

    @Override
    public Boolean getbit(String arg0, long arg1) {
        return read(j -> j.getbit(arg0, arg1));
    }

    @Override
    public String getrange(String arg0, long arg1, long arg2) {
        return read(j -> j.getrange(arg0, arg1, arg2));
    }

    @Override
    public Long hdel(String arg0, String... arg1) {
        return write(j -> j.hdel(arg0, arg1));
    }

    @Override
    public Boolean hexists(String arg0, String arg1) {
        return read(j -> j.hexists(arg0, arg1));
    }

    @Override
    public String hget(String arg0, String arg1) {
        return read(j -> j.hget(arg0, arg1));
    }

    @Override
    public Map<String, String> hgetAll(String arg0) {        
        return read(j -> j.hgetAll(arg0));
    }

    @Override
    public Long hincrBy(String arg0, String arg1, long arg2) {
        return write(j -> j.hincrBy(arg0, arg1, arg2));
    }

    @Override
    public Set<String> hkeys(String arg0) {
        return read(j -> j.hkeys(arg0));
    }

    @Override
    public Long hlen(String arg0) {
        return read(j -> j.hlen(arg0));
    }

    @Override
    public List<String> hmget(String arg0, String... arg1) {
        return read(j -> j.hmget(arg0, arg1));
    }

    @Override
    public String hmset(String arg0, Map<String, String> arg1) {
        return write(j -> j.hmset(arg0, arg1));
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, int arg1) {
        return read(j -> j.hscan(arg0, arg1));
    }

    @Override
    public ScanResult<Entry<String, String>> hscan(String arg0, String arg1) {
        return read(j -> j.hscan(arg0, arg1));
    }

    @Override
    public Long hset(String arg0, String arg1, String arg2) {
        return write(j -> j.hset(arg0, arg1, arg2));
    }

    @Override
    public Long hsetnx(String arg0, String arg1, String arg2) {
        return write(j -> j.hsetnx(arg0, arg1, arg2));
    }

    @Override
    public List<String> hvals(String arg0) {
        return read(j -> j.hvals(arg0));
    }

    @Override
    public Long incr(String arg0) {
        return write(j -> j.incr(arg0));
    }

    @Override
    public Long incrBy(String arg0, long arg1) {
        return write(j -> j.incrBy(arg0, arg1));
    }

    @Override
    public String lindex(String arg0, long arg1) {
        return read(j -> j.lindex(arg0, arg1));
    }

    @Override
    public Long linsert(String arg0, LIST_POSITION arg1, String arg2, String arg3) {
        return write(j -> j.linsert(arg0, arg1, arg2, arg3));
    }

    @Override
    public Long llen(String arg0) {
        return read(j -> j.llen(arg0));
    }

    @Override
    public String lpop(String arg0) {
        return write(j -> j.lpop(arg0));
    }

    @Override
    public Long lpush(String arg0, String... arg1) {
        return write(j -> j.lpush(arg0, arg1));
    }

    @Override
    public Long lpushx(String arg0, String... arg1) {
        return write(j -> j.lpushx(arg0, arg1));
    }

    @Override
    public List<String> lrange(String arg0, long arg1, long arg2) {
        return read(j -> j.lrange(arg0, arg1, arg2));
    }

    @Override
    public Long lrem(String arg0, long arg1, String arg2) {
        return write(j -> j.lrem(arg0, arg1, arg2));
    }

    @Override
    public String lset(String arg0, long arg1, String arg2) {
        return write(j -> j.lset(arg0, arg1, arg2));
    }

    @Override
    public String ltrim(String arg0, long arg1, long arg2) {
        return write(j -> j.ltrim(arg0, arg1, arg2));
    }

    @Override
    public Long move(String arg0, int arg1) {
        return write(j -> j.move(arg0, arg1));
    }

    @Override
    public Long persist(String arg0) {
        return write(j -> j.persist(arg0));
    }

    @Override
    public Long pexpire(String arg0, long arg1) {
        return write(j -> j.pexpire(arg0, arg1));
    }

    @Override
    public Long pexpireAt(String arg0, long arg1) {
        return write(j -> j.pexpireAt(arg0, arg1));
    }

    @Override
    public Long pfadd(String arg0, String... arg1) {
        return write(j -> j.pfadd(arg0, arg1));
    }

    @Override
    public long pfcount(String arg0) {
        return read(j -> j.pfcount(arg0));
    }

    @Override
    public String rpop(String arg0) {
        return write(j -> j.rpop(arg0));
    }

    @Override
    public Long rpush(String arg0, String... arg1) {
        return write(j -> j.rpush(arg0, arg1));
    }

    @Override
    public Long rpushx(String arg0, String... arg1) {
        return write(j -> j.rpushx(arg0, arg1));
    }

    @Override
    public Long sadd(String arg0, String... arg1) {
        return write(j -> j.sadd(arg0, arg1));
    }

    @Override
    public Long scard(String arg0) {
        return read(j -> j.scard(arg0));
    }

    @Override
    public String set(String arg0, String arg1) {
        return write(j -> j.set(arg0, arg1));
    }

    @Override
    public String set(String arg0, String arg1, String arg2, String arg3, long arg4) {
        return write(j -> j.set(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Boolean setbit(String arg0, long arg1, boolean arg2) {
        return write(j -> j.setbit(arg0, arg1, arg2));
    }

    @Override
    public Boolean setbit(String arg0, long arg1, String arg2) {
        return write(j -> j.setbit(arg0, arg1, arg2));
    }

    @Override
    public String setex(String arg0, int arg1, String arg2) {
        return write(j -> j.setex(arg0, arg1, arg2));
    }

    @Override
    public Long setnx(String arg0, String arg1) {
        return write(j -> j.setnx(arg0, arg1));
    }

    @Override
    public Long setrange(String arg0, long arg1, String arg2) {
        return write(j -> j.setrange(arg0, arg1, arg2));
    }

    @Override
    public Boolean sismember(String arg0, String arg1) {
        return read(j -> j.sismember(arg0, arg1));
    }

    @Override
    public Set<String> smembers(String arg0) {
        return read(j -> j.smembers(arg0));
    }

    @Override
    public List<String> sort(String arg0) {
        return write(j -> j.sort(arg0));
    }

    @Override
    public List<String> sort(String arg0, SortingParams arg1) {
        return write(j -> j.sort(arg0, arg1));
    }

    @Override
    public String spop(String arg0) {
        return write(j -> j.spop(arg0));
    }

    @Override
    public String srandmember(String arg0) {
        return read(j -> j.srandmember(arg0));
    }

    @Override
    public List<String> srandmember(String arg0, int arg1) {
        return read(j -> j.srandmember(arg0, arg1));
    }

    @Override
    public Long srem(String arg0, String... arg1) {
        return write(j -> j.srem(arg0));
    }

    @Override
    public ScanResult<String> sscan(String arg0, int arg1) {
        return read(j -> j.sscan(arg0, arg1));
    }

    @Override
    public ScanResult<String> sscan(String arg0, String arg1) {
        return read(j -> j.sscan(arg0, arg1));
    }

    @Override
    public Long strlen(String arg0) {
        return read(j -> j.strlen(arg0));
    }

    @Override
    public String substr(String arg0, int arg1, int arg2) { // TODO which redis command is this ?
        return read(j -> j.substr(arg0, arg1, arg2));
    }

    @Override
    public Long ttl(String arg0) {
        return read(j -> j.ttl(arg0));
    }

    @Override
    public String type(String arg0) {
        return read(j -> j.type(arg0));
    }

    @Override
    public Long zadd(String arg0, Map<String, Double> arg1) {
        return write(j -> j.zadd(arg0, arg1));
    }

    @Override
    public Long zadd(String arg0, double arg1, String arg2) {
        return write(j -> j.zadd(arg0, arg1, arg2));
    }

    @Override
    public Long zcard(String arg0) {
        return read(j -> j.zcard(arg0));
    }

    @Override
    public Long zcount(String arg0, double arg1, double arg2) {
        return read(j -> j.zcount(arg0, arg1, arg2));
    }

    @Override
    public Long zcount(String arg0, String arg1, String arg2) {
        return read(j -> j.zcount(arg0, arg1, arg2));
    }

    @Override
    public Double zincrby(String arg0, double arg1, String arg2) {
        return write(j -> j.zincrby(arg0, arg1, arg2));
    }

    @Override
    public Long zlexcount(String arg0, String arg1, String arg2) {
        return read(j -> j.zlexcount(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrange(String arg0, long arg1, long arg2) {
        return read(j -> j.zrange(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrangeByLex(String arg0, String arg1, String arg2) {
        return read(j -> j.zrangeByLex(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrangeByLex(String arg0, String arg1, String arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByLex(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<String> zrangeByScore(String arg0, double arg1, double arg2) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrangeByScore(String arg0, String arg1, String arg2) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrangeByScore(String arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<String> zrangeByScore(String arg0, String arg1, String arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, double arg1, double arg2) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, String arg1, String arg2) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String arg0, String arg1, String arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrangeWithScores(String arg0, long arg1, long arg2) {
        return read(j -> j.zrangeWithScores(arg0, arg1, arg2));
    }

    @Override
    public Long zrank(String arg0, String arg1) {
        return read(j -> j.zrank(arg0, arg1));
    }

    @Override
    public Long zrem(String arg0, String... arg1) {
        return write(j -> j.zrem(arg0, arg1));
    }

    @Override
    public Long zremrangeByLex(String arg0, String arg1, String arg2) {
        return write(j -> j.zremrangeByLex(arg0, arg1, arg2));
    }

    @Override
    public Long zremrangeByRank(String arg0, long arg1, long arg2) {
        return write(j -> j.zremrangeByRank(arg0, arg1, arg2));
    }

    @Override
    public Long zremrangeByScore(String arg0, double arg1, double arg2) {
        return write(j -> j.zremrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Long zremrangeByScore(String arg0, String arg1, String arg2) {
        return write(j -> j.zremrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrevrange(String arg0, long arg1, long arg2) {
        return read(j -> j.zrevrange(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, double arg1, double arg2) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, String arg1, String arg2) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<String> zrevrangeByScore(String arg0, String arg1, String arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, double arg1, double arg2) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, String arg1, String arg2) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String arg0, String arg1, String arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String arg0, long arg1, long arg2) {
        return read(j -> j.zrevrangeWithScores(arg0, arg1, arg2));
    }

    @Override
    public Long zrevrank(String arg0, String arg1) {
        return read(j -> j.zrevrank(arg0, arg1));
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, int arg1) {
        return read(j -> j.zscan(arg0, arg1));
    }

    @Override
    public ScanResult<Tuple> zscan(String arg0, String arg1) {
        return read(j -> j.zscan(arg0, arg1));
    }

    @Override
    public Double zscore(String arg0, String arg1) {
        return read(j -> j.zscore(arg0, arg1));
    }

    // ---- redis.clients.jedis.BinaryJedisCommands
    
    @Override
    public Long append(byte[] arg0, byte[] arg1) {
        return write(j -> j.append(arg0, arg1));
    }

    @Override
    public Long bitcount(byte[] arg0) {
        return read(j -> j.bitcount(arg0));
    }

    @Override
    public Long bitcount(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.bitcount(arg0, arg1, arg2));
    }

    @Override
    public List<byte[]> blpop(byte[] arg0) {
        return write(j -> j.blpop(arg0));
    }

    @Override
    public List<byte[]> brpop(byte[] arg0) {
        return write(j -> j.brpop(arg0));
    }

    @Override
    public Long decr(byte[] arg0) {
        return write(j -> j.decr(arg0));
    }

    @Override
    public Long decrBy(byte[] arg0, long arg1) {
        return write(j -> j.decrBy(arg0, arg1));
    }

    @Override
    public Long del(byte[] arg0) {
        return write(j -> j.del(arg0));
    }

    @Override
    public byte[] echo(byte[] arg0) {
        return read(j -> j.echo(arg0));
    }

    @Override
    public Boolean exists(byte[] arg0) {
        return write(j -> j.exists(arg0));
    }

    @Override
    public Long expire(byte[] arg0, int arg1) {
        return write(j -> j.expire(arg0, arg1));
    }

    @Override
    public Long expireAt(byte[] arg0, long arg1) {
        return write(j -> j.expireAt(arg0, arg1));
    }

    @Override
    public byte[] get(byte[] arg0) {
        return read(j -> j.get(arg0));
    }

    @Override
    public byte[] getSet(byte[] arg0, byte[] arg1) {
        return write(j -> j.getSet(arg0, arg1));
    }

    @Override
    public Boolean getbit(byte[] arg0, long arg1) {
        return read(j -> j.getbit(arg0, arg1));
    }

    @Override
    public byte[] getrange(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.getrange(arg0, arg1, arg2));
    }

    @Override
    public Long hdel(byte[] arg0, byte[]... arg1) {
        return write(j -> j.hdel(arg0, arg1));
    }

    @Override
    public Boolean hexists(byte[] arg0, byte[] arg1) {
        return read(j -> j.hexists(arg0, arg1));
    }

    @Override
    public byte[] hget(byte[] arg0, byte[] arg1) {
        return read(j -> j.hget(arg0, arg1));
    }

    @Override
    public Map<byte[], byte[]> hgetAll(byte[] arg0) {
        return read(j -> j.hgetAll(arg0));
    }

    @Override
    public Long hincrBy(byte[] arg0, byte[] arg1, long arg2) {
        return write(j -> j.hincrBy(arg0, arg1, arg2));
    }

    @Override
    public Double hincrByFloat(byte[] arg0, byte[] arg1, double arg2) {
        return write(j -> j.hincrByFloat(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> hkeys(byte[] arg0) {
        return read(j -> j.hkeys(arg0));
    }

    @Override
    public Long hlen(byte[] arg0) {
        return read(j -> j.hlen(arg0));
    }

    @Override
    public List<byte[]> hmget(byte[] arg0, byte[]... arg1) {
        return read(j -> j.hmget(arg0, arg1));
    }

    @Override
    public String hmset(byte[] arg0, Map<byte[], byte[]> arg1) {
        return write(j -> j.hmset(arg0, arg1));
    }

    @Override
    public Long hset(byte[] arg0, byte[] arg1, byte[] arg2) {
        return write(j -> j.hset(arg0, arg1, arg2));
    }

    @Override
    public Long hsetnx(byte[] arg0, byte[] arg1, byte[] arg2) {
        return write(j -> j.hsetnx(arg0, arg1, arg2));
    }

    @Override
    public Collection<byte[]> hvals(byte[] arg0) {
        return read(j -> j.hvals(arg0));
    }

    @Override
    public Long incr(byte[] arg0) {
        return write(j -> j.incr(arg0));
    }

    @Override
    public Long incrBy(byte[] arg0, long arg1) {
        return write(j -> j.incrBy(arg0, arg1));
    }

    @Override
    public Double incrByFloat(byte[] arg0, double arg1) {
        return write(j -> j.incrByFloat(arg0, arg1));
    }

    @Override
    public byte[] lindex(byte[] arg0, long arg1) {
        return read(j -> j.lindex(arg0, arg1));
    }

    @Override
    public Long linsert(byte[] arg0, LIST_POSITION arg1, byte[] arg2, byte[] arg3) {
        return write(j -> j.linsert(arg0, arg1, arg2, arg3));
    }

    @Override
    public Long llen(byte[] arg0) {
        return read(j -> j.llen(arg0));
    }

    @Override
    public byte[] lpop(byte[] arg0) {
        return write(j -> j.lpop(arg0));
    }

    @Override
    public Long lpush(byte[] arg0, byte[]... arg1) {
        return write(j -> j.lpush(arg0, arg1));
    }

    @Override
    public Long lpushx(byte[] arg0, byte[]... arg1) {
        return write(j -> j.lpushx(arg0, arg1));
    }

    @Override
    public List<byte[]> lrange(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.lrange(arg0, arg1, arg2));
    }

    @Override
    public Long lrem(byte[] arg0, long arg1, byte[] arg2) {
        return write(j -> j.lrem(arg0, arg1, arg2));
    }

    @Override
    public String lset(byte[] arg0, long arg1, byte[] arg2) {
        return write(j -> j.lset(arg0, arg1, arg2));
    }

    @Override
    public String ltrim(byte[] arg0, long arg1, long arg2) {
        return write(j -> j.ltrim(arg0, arg1, arg2));
    }

    @Override
    public Long move(byte[] arg0, int arg1) {
        return write(j -> j.move(arg0, arg1));
    }

    @Override
    public Long persist(byte[] arg0) {
        return write(j -> j.persist(arg0));
    }

    @Override
    public Long pexpireAt(byte[] arg0, long arg1) {
        return write(j -> j.pexpireAt(arg0, arg1));
    }

    @Override
    public Long pfadd(byte[] arg0, byte[]... arg1) {
        return write(j -> j.pfadd(arg0, arg1));
    }

    @Override
    public long pfcount(byte[] arg0) {
        return read(j -> j.pfcount(arg0));
    }

    @Override
    public byte[] rpop(byte[] arg0) {
        return write(j -> j.rpop(arg0));
    }

    @Override
    public Long rpush(byte[] arg0, byte[]... arg1) {
        return write(j -> j.rpush(arg0, arg1));
    }

    @Override
    public Long rpushx(byte[] arg0, byte[]... arg1) {
        return write(j -> j.rpushx(arg0, arg1));
    }

    @Override
    public Long sadd(byte[] arg0, byte[]... arg1) {
        return write(j -> j.sadd(arg0, arg1));
    }

    @Override
    public Long scard(byte[] arg0) {
        return read(j -> j.scard(arg0));
    }

    @Override
    public String set(byte[] arg0, byte[] arg1) {
        return write(j -> j.set(arg0, arg1));
    }

    @Override
    public Boolean setbit(byte[] arg0, long arg1, boolean arg2) {
        return write(j -> j.setbit(arg0, arg1, arg2));
    }

    @Override
    public Boolean setbit(byte[] arg0, long arg1, byte[] arg2) {
        return write(j -> j.setbit(arg0, arg1, arg2));
    }

    @Override
    public String setex(byte[] arg0, int arg1, byte[] arg2) {
        return write(j -> j.setex(arg0, arg1, arg2));
    }

    @Override
    public Long setnx(byte[] arg0, byte[] arg1) {
        return write(j -> j.setnx(arg0, arg1));
    }

    @Override
    public Long setrange(byte[] arg0, long arg1, byte[] arg2) {
        return write(j -> j.setrange(arg0, arg1, arg2));
    }

    @Override
    public Boolean sismember(byte[] arg0, byte[] arg1) {
        return read(j -> j.sismember(arg0, arg1));
    }

    @Override
    public Set<byte[]> smembers(byte[] arg0) {
        return read(j -> j.smembers(arg0));
    }

    @Override
    public List<byte[]> sort(byte[] arg0) {
        return write(j -> j.sort(arg0));
    }

    @Override
    public List<byte[]> sort(byte[] arg0, SortingParams arg1) {
        return write(j -> j.sort(arg0, arg1));
    }

    @Override
    public byte[] spop(byte[] arg0) {
        return write(j -> j.spop(arg0));
    }

    @Override
    public byte[] srandmember(byte[] arg0) {
        return read(j -> j.srandmember(arg0));
    }

    @Override
    public List<byte[]> srandmember(byte[] arg0, int arg1) {
        return read(j -> j.srandmember(arg0, arg1));
    }

    @Override
    public Long srem(byte[] arg0, byte[]... arg1) {
        return write(j -> j.srem(arg0));
    }

    @Override
    public Long strlen(byte[] arg0) {
        return read(j -> j.strlen(arg0));
    }

    @Override
    public byte[] substr(byte[] arg0, int arg1, int arg2) { // TODO which redis command is this ?
        return read(j -> j.substr(arg0, arg1, arg2));
    }

    @Override
    public Long ttl(byte[] arg0) {
        return read(j -> j.ttl(arg0));
    }

    @Override
    public String type(byte[] arg0) {
        return read(j -> j.type(arg0));
    }

    @Override
    public Long zadd(byte[] arg0, Map<byte[], Double> arg1) {
        return write(j -> j.zadd(arg0, arg1));
    }

    @Override
    public Long zadd(byte[] arg0, double arg1, byte[] arg2) {
        return write(j -> j.zadd(arg0, arg1, arg2));
    }

    @Override
    public Long zcard(byte[] arg0) {
        return read(j -> j.zcard(arg0));
    }

    @Override
    public Long zcount(byte[] arg0, double arg1, double arg2) {
        return read(j -> j.zcount(arg0, arg1, arg2));
    }

    @Override
    public Long zcount(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zcount(arg0, arg1, arg2));
    }

    @Override
    public Double zincrby(byte[] arg0, double arg1, byte[] arg2) {
        return write(j -> j.zincrby(arg0, arg1, arg2));
    }

    @Override
    public Long zlexcount(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zlexcount(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrange(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.zrange(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zrangeByLex(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByLex(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, double arg1, double arg2) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<byte[]> zrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, double arg1, double arg2) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
        return read(j -> j.zrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrangeWithScores(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.zrangeWithScores(arg0, arg1, arg2));
    }

    @Override
    public Long zrank(byte[] arg0, byte[] arg1) {
        return read(j -> j.zrank(arg0, arg1));
    }

    @Override
    public Long zrem(byte[] arg0, byte[]... arg1) {
        return write(j -> j.zrem(arg0, arg1));
    }

    @Override
    public Long zremrangeByLex(byte[] arg0, byte[] arg1, byte[] arg2) {
        return write(j -> j.zremrangeByLex(arg0, arg1, arg2));
    }

    @Override
    public Long zremrangeByRank(byte[] arg0, long arg1, long arg2) {
        return write(j -> j.zremrangeByRank(arg0, arg1, arg2));
    }

    @Override
    public Long zremrangeByScore(byte[] arg0, double arg1, double arg2) {
        return write(j -> j.zremrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Long zremrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2) {
        return write(j -> j.zremrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrevrange(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.zrevrange(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, double arg1, double arg2) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<byte[]> zrevrangeByScore(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScore(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, double arg1, double arg2) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, double arg1, double arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(byte[] arg0, byte[] arg1, byte[] arg2, int arg3, int arg4) {
        return read(j -> j.zrevrangeByScoreWithScores(arg0, arg1, arg2, arg3, arg4));
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(byte[] arg0, long arg1, long arg2) {
        return read(j -> j.zrevrangeWithScores(arg0, arg1, arg2));
    }

    @Override
    public Long zrevrank(byte[] arg0, byte[] arg1) {
        return read(j -> j.zrevrank(arg0, arg1));
    }

    @Override
    public Double zscore(byte[] arg0, byte[] arg1) {
        return read(j -> j.zscore(arg0, arg1));
    }
    
}
