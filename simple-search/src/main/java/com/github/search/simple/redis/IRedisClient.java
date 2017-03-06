package com.github.search.simple.redis;

import redis.clients.jedis.Client;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.SortingParams;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Arvin
 * @time 2017/1/9 22:37
 */
public interface IRedisClient extends JedisProvider {

    // 基础命令 开始 ------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 删除当前所选数据库所有的 redis keys
     * <p>
     * Delete all the keys of the currently selected DB. This command never
     * fails.
     *
     * @return Status code reply
     */
    String flushDB();

    /**
     * 返回当前选择的数据库 keys 的总数量
     * <p>
     * Return the number of keys in the currently selected database.
     *
     * @return Long reply
     */
    Long dbSize();

    /**
     * 选择一个数据库，从0开始默认选择的是 DB 0
     * <p>
     * Select the DB with having the specified zero-based numeric index. For
     * default every new client connection is automatically selected to DB 0.
     *
     * @param index 要选择的数据库，从0开始
     * @return 状态码
     */
    String select(int index);

    /**
     * 删除所有存在的数据库的所有keys
     * <p>
     * Delete all the keys of all the existing databases, not just the currently
     * selected one. This command never fails.
     *
     * @return Status code reply
     */
    String flushAll();
    // 基础命令 结束 ------------------------------------------------------------------------------------------------------------------------------------

    // 基础JedisCommands命令 开始 ------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 设置指定Key的值
     *
     * @param key   键
     * @param value 值
     * @return 返回状态码
     */
    String set(String key, String value);

    /**
     * 获取指定key的字符串值
     *
     * @param key 键
     */
    String get(String key);

    /**
     * 校验指定的key是否存在
     *
     * @param key 键
     */
    boolean exists(String key);

    /**
     * 移除给定 key 的生存时间，将这个 key 从『可挥发』的(带生存时间 key )转换成『持久化』的(一个不带生存时间、永不过期的 key )。
     *
     * @param key 键
     * @return 当生存时间移除成功时，返回 true . 如果 key 不存在或 key 没有设置生存时间，返回 false
     */
    boolean persist(String key);

    /**
     * <pre>
     * 返回 key 所储存的值的类型，结果类型包含：{@link RedisVarType}
     *  none    (key不存在)
     *  string  (字符串)
     *  list    (列表)
     *  set     (集合)
     *  zset    (有序集)
     *  hash    (哈希表)
     * </pre>
     *
     * @param key 键
     */
    String type(String key);

    /**
     * <pre>
     * 为给定 key 设置生存时间，当 key 过期时(生存时间为 0 )，它会被自动删除。
     * 在 Redis 中，带有生存时间的 key 被称为『可挥发』(volatile)的。
     * 生存时间可以通过使用 DEL 命令来删除整个 key 来移除，或者被 SET 和 GETSET 命令覆写(overwrite)，这意味着，
     * 如果一个命令只是修改(alter)一个带生存时间的 key 的值而不是用一个新的 key 值来代替(replace)它的话，那么生存时间不会被改变。
     *
     * 比如说，对一个 key 执行 INCR 命令，对一个列表进行 LPUSH 命令，或者对一个哈希表执行 HSET 命令，这类操作都不会修改 key 本身的生存时间。
     * 另一方面，如果使用 RENAME 对一个 key 进行改名，那么改名后的 key 的生存时间和改名前一样。
     *
     * RENAME 命令的另一种可能是，尝试将一个带生存时间的 key 改名成另一个带生存时间的 another_key ，这时旧的 another_key
     * (以及它的生存时间)会被删除，然后旧的 key 会改名为 another_key ，因此，新的 another_key 的生存时间也和原本的 key 一样。
     * 使用 PERSIST 命令可以在不删除 key 的情况下，移除 key 的生存时间，让 key 重新成为一个『持久化』(persistent) key 。
     *
     * 更新生存时间
     *  可以对一个已经带有生存时间的 key 执行 EXPIRE 命令，新指定的生存时间会取代旧的生存时间。
     *
     * 过期时间的精确度
     *  在 Redis 2.4 版本中，过期时间的延迟在 1 秒钟之内 —— 也即是，就算 key 已经过期，但它还是可能在过期之后一秒钟之内被访问到，
     *  而在新的 Redis 2.6 版本中，延迟被降低到 1 毫秒之内。
     * </pre>
     *
     * @param key     键
     * @param seconds 秒
     * @return 设置成功返回 true 当 key 不存在或者不能为 key 设置生存时间时(比如在低于 2.1.3 版本的 Redis 中你尝试更新 key 的生存时间)，返回 false 。
     */
    boolean expire(String key, int seconds);

    Long expireAt(String key, long unixTime);

    Long ttl(String key);

    Boolean setbit(String key, long offset, boolean value);

    Boolean setbit(String key, long offset, String value);

    Boolean getbit(String key, long offset);

    Long setrange(String key, long offset, String value);

    String getrange(String key, long startOffset, long endOffset);

    String getSet(String key, String value);

    Long setnx(String key, String value);

    String setex(String key, int seconds, String value);

    Long decrBy(String key, long integer);

    Long decr(String key);

    Long incrBy(String key, long integer);

    Long incr(String key);

    Long append(String key, String value);

    String substr(String key, int start, int end);

    Long hset(String key, String field, String value);

    String hget(String key, String field);

    Long hsetnx(String key, String field, String value);

    String hmset(String key, Map<String, String> hash);

    List<String> hmget(String key, String... fields);

    Long hincrBy(String key, String field, long value);

    Boolean hexists(String key, String field);

    Long hdel(String key, String... field);

    Long hlen(String key);

    Set<String> hkeys(String key);

    List<String> hvals(String key);

    Map<String, String> hgetAll(String key);

    Long rpush(String key, String... string);

    Long lpush(String key, String... string);

    Long llen(String key);

    List<String> lrange(String key, long start, long end);

    String ltrim(String key, long start, long end);

    String lindex(String key, long index);

    String lset(String key, long index, String value);

    Long lrem(String key, long count, String value);

    String lpop(String key);

    String rpop(String key);

    Long sadd(String key, String... member);

    Set<String> smembers(String key);

    Long srem(String key, String... member);

    String spop(String key);

    Long scard(String key);

    Boolean sismember(String key, String member);

    String srandmember(String key);

    Long strlen(String key);

    Long zadd(String key, double score, String member);

    Long zadd(String key, Map<String, Double> scoreMembers);

    Set<String> zrange(String key, long start, long end);

    Long zrem(String key, String... member);

    Double zincrby(String key, double score, String member);

    Long zrank(String key, String member);

    Long zrevrank(String key, String member);

    Set<String> zrevrange(String key, long start, long end);

    Set<Tuple> zrangeWithScores(String key, long start, long end);

    Set<Tuple> zrevrangeWithScores(String key, long start, long end);

    Long zcard(String key);

    Double zscore(String key, String member);

    List<String> sort(String key);

    List<String> sort(String key, SortingParams sortingParameters);

    Long zcount(String key, double min, double max);

    Long zcount(String key, String min, String max);

    Set<String> zrangeByScore(String key, double min, double max);

    Set<String> zrangeByScore(String key, String min, String max);

    Set<String> zrevrangeByScore(String key, double max, double min);

    Set<String> zrangeByScore(String key, double min, double max, int offset,
                              int count);

    Set<String> zrevrangeByScore(String key, String max, String min);

    Set<String> zrangeByScore(String key, String min, String max, int offset,
                              int count);

    Set<String> zrevrangeByScore(String key, double max, double min,
                                 int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min);

    Set<Tuple> zrangeByScoreWithScores(String key, double min, double max,
                                       int offset, int count);

    Set<String> zrevrangeByScore(String key, String max, String min,
                                 int offset, int count);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min);

    Set<Tuple> zrangeByScoreWithScores(String key, String min, String max,
                                       int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min,
                                          int offset, int count);

    Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min,
                                          int offset, int count);

    Long zremrangeByRank(String key, long start, long end);

    Long zremrangeByScore(String key, double start, double end);

    Long zremrangeByScore(String key, String start, String end);

    Long linsert(String key, Client.LIST_POSITION where, String pivot,
                 String value);

    Long lpushx(String key, String... string);

    Long rpushx(String key, String... string);

    List<String> blpop(String arg);

    List<String> brpop(String arg);

    Long del(String key);

    String echo(String string);

    Long move(String key, int dbIndex);

    Long bitcount(final String key);

    Long bitcount(final String key, long start, long end);

    @Deprecated
    /**
     * This method is deprecated due to bug (scan cursor should be unsigned long)
     * And will be removed on next major release
     * @see https://github.com/xetorthio/jedis/issues/531
     */
    ScanResult<Map.Entry<String, String>> hscan(final String key, int cursor);

    @Deprecated
    /**
     * This method is deprecated due to bug (scan cursor should be unsigned long)
     * And will be removed on next major release
     * @see https://github.com/xetorthio/jedis/issues/531
     */
    ScanResult<String> sscan(final String key, int cursor);

    @Deprecated
    /**
     * This method is deprecated due to bug (scan cursor should be unsigned long)
     * And will be removed on next major release
     * @see https://github.com/xetorthio/jedis/issues/531
     */
    ScanResult<Tuple> zscan(final String key, int cursor);

    ScanResult<Map.Entry<String, String>> hscan(final String key, final String cursor);

    ScanResult<String> sscan(final String key, final String cursor);

    ScanResult<Tuple> zscan(final String key, final String cursor);

    // 基础JedisCommands命令 结束 ------------------------------------------------------------------------------------------------------------------------------------


}
