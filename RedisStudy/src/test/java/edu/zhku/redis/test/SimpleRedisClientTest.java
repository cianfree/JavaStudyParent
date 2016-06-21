package edu.zhku.redis.test;

import org.junit.Test;

/**
 * 简单使用Redis Client
 */
public class SimpleRedisClientTest extends BaseClient {

    /**
     *
     */
    @Test
    public void testBlankKey() {
        String key = "hello world";
        String ret = jedis.set(key, "Hello");

        System.out.println(ret);
    }

    @Test
    public void testSetCommand() throws InterruptedException {
        String dataKey = "name";

        // 删除KEY 为dataKey的数据, 如果删除的key不存在，则直接忽略；返回整型，被删除的keys的数量
        Long delCount = jedis.del(dataKey);
        logger.info("Delete [" + dataKey + "]: " + delCount);

        // 存数据, 第三个参数： XX表示存在了才会设置，NX表示不存在的情况下才设置
        logger.info("Set [" + dataKey + "] with XX!");
        String result = jedis.set(dataKey, "Arvin", "XX");
        logger.info("After Set(XX) Return: " + result);

        logger.info("Set [" + dataKey + "] with NX!");
        result = jedis.set(dataKey, "夏集球", "NX");
        logger.info("After Set(NX) Return: " + result);

        // 测试指定时间过期的数据,使用以下接口
        /*
        public String set(
            final String key,
            final String value,
            final String nxxx, // NX|XX, NX -- Only set the key if it does not already exist. XX -- Only set the key if it already exist.
            final String expx, // EX|PX, expire time units: EX = seconds; PX = milliseconds
            final long time) // expire time in the units of {@param #expx}
        */
        delCount = jedis.del("expire");
        logger.info("Delete key[expire]: " + delCount);
        int expireTimeInMS = 3000;
        String expx = "PX"; // 单位为毫秒
        logger.info("Invoke set(key, value, nxxx, expx, time): [" + expireTimeInMS + "] milliseconds will expired!");
        jedis.set("expire", "test expire", "NX", expx, expireTimeInMS);
        logger.info("Sleep [" + expireTimeInMS + 500 + "] milliseconds!");
        Thread.sleep(expireTimeInMS + 500);
        String expire = jedis.get("expire");
        logger.info("After [" + expireTimeInMS + 500 + "] milliseconds then get expire = [" + expire + "]");
    }

    /**
     * 测试Get命令
     */
    @Test
    public void testGetCommand() {
        // 获取一个不存在的值
        jedis.del("name");
        String name = jedis.get("name");
        logger.info("Get not exists key[name]=" + name);
        jedis.set("name", "夏集球");

        // 获取一个存在的之
        name = jedis.get("name");
        logger.info("Get exists key[name]=" + name);
        jedis.set("name", "夏集球");

        jedis.del("name");
    }

    /**
     * 测试Del删除命令
     */
    @Test
    public void testDelCommand() {
        jedis.set("del1", "1");
        jedis.set("del2", "2");
        jedis.set("del3", "3");
        jedis.set("del4", "4");

        Long delCount = jedis.del("del1");
        logger.info("Delete [del1]: " + delCount);

        delCount = jedis.del("del2", "del3", "del4");
        logger.info("Delete [del2, del3, del4]: " + delCount);

        delCount = jedis.del("NotExists");
        logger.info("Delete not exists key: " + delCount);
    }

    /**
     * 测试exists命令
     */
    @Test
    public void testExistsCommand() {
        String key = "helloExists";
        jedis.set(key, "1");
        Boolean exists = jedis.exists(key);
        logger.info("Check exists key: " + exists);

        jedis.del(key);
        exists = jedis.exists(key);
        logger.info("Check not exists key: " + exists);
    }

    /**
     * <pre>
     * Append 命令测试, 返回字符串的长度
     * 如果 key 已经存在，并且值为字符串，那么这个命令会把 value 追加到原来值（value）的结尾。
     * 如果 key 不存在，那么它将首先创建一个空字符串的key，再执行追加操作，这种情况 APPEND 将类似于 SET 操作。
     * 返回值
     * Integer reply：返回append后字符串值（value）的长度。
     * </pre>
     */
    @Test
    public void testAppendCommand() {
        String key = "append";
        Long result = jedis.append(key, "Hello");
        logger.info("Append for not exists key : " + result + ", " + jedis.get(key));

        result = jedis.append(key, ",");
        logger.info("Append for exists key : " + result + ", " + jedis.get(key));

        jedis.del(key);

    }

    /**
     * 测试Incr命令：
     * 对存储在指定key的数值执行原子的加1操作。
     * 如果指定的key不存在，那么在执行incr操作之前，会先将它的值设定为0。
     * 如果指定的key中存储的值不是字符串类型（fix：）或者存储的字符串类型不能表示为一个整数，
     * 那么执行这个命令时服务器会返回一个错误(eq:(error) ERR value is not an integer or out of range)。
     * 这个操作仅限于64位的有符号整型数据。
     * 注意: 由于redis并没有一个明确的类型来表示整型数据，所以这个操作是一个字符串操作。
     * 执行这个操作的时候，key对应存储的字符串被解析为10进制的64位有符号整型数据。
     * 事实上，Redis 内部采用整数形式（Integer representation）来存储对应的整数值，所以对该类字符串值实际上是用整数保存，也就不存在存储整数的字符串表示（String representation）所带来的额外消耗。
     * 返回值
     * integer-reply:执行递增操作后key对应的值。
     */
    @Test
    public void testIncrCommand() {
        String key = "key";
        // 第一次，返回为1
        Long count = jedis.incr(key);
        logger.info("Increase [" + key + "] with not exists before: " + count);

        count = jedis.incr(key);
        logger.info("Increase [" + key + "] with exists before: " + count);

        jedis.del(key);
    }

    /**
     * <pre>
     *  对key对应的数字做减1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。
     *  如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。这个操作最大支持在64位有符号的整型数字。
     *
     *  查看命令INCR了解关于增减操作的额外信息。
     *  返回值
     *  数字：减小之后的value
     * </pre>
     */
    @Test
    public void testDecrCommand() {
        String key = "count";
        // 第一次，返回为-1
        Long count = jedis.decr(key);
        logger.info("Decrease [" + key + "] with not exists before: " + count);

        count = jedis.decr(key);
        logger.info("Decrease [" + key + "] with exists before: " + count);

        jedis.del(key);
    }

}
