package com.fsmflying.redis01;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RedisTest01 {

    Jedis jedis = null;
    //    JedisPoolConfig jedisPoolConfig = null;
    JedisPool jedisPool;

    @Before
    public void before() {
        jedis = new Jedis("192.168.2.20", 6379);
        //jedisPoolConfig = new JedisPoolConfig();
        jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.2.20");
    }

    @After
    public void after() {
        jedis.close();
        jedisPool.destroy();
    }

    @Test
    public void test00() {
        jedis.set("jedis", "2.9");
    }

    @Test
    public void test01_set_write() {
        System.out.println("-----------------------------------------------");
        jedis.sadd("set_users", "fangming", "yanxia", "wangxiaojuan", "zhangyang");
    }

    @Test
    public void test02_set_read() {
        System.out.println("-----------------------------------------------");
        String type = jedis.type("set_users");
        System.out.println("type:" + type);
        System.out.println("--members---------------------------------------------");
        Set<String> users = jedis.smembers("set_users");
        for (String s : users)
            System.out.println(s);
    }

    @Test
    public void test11_list_lpush() {
        System.out.println("--list--lpush---------------------------------------------");
        jedis.lpush("list_users", "fangming", "yanxia", "wangxiaojuan", "zhangyang");

    }

    @Test
    public void test12_list_lrange() {
        System.out.println("---list-lrange--------------------------------------------");
        List<String> users = jedis.lrange("list_users", 0, 100);
        for (String s : users)
            System.out.println(s);
    }

    @Test
    public void test13_list_lpop() {
        System.out.println("---list--lpop--------------------------------------------");
        String user = jedis.lpop("list_users");

        for (int i = 0; i < 10; i++) {
            user = jedis.lpop("list_users");
            System.out.println(user);
            if (user == null || user.equals(""))
                break;
        }
    }

    @Test
    public void test14_list_lset() {
        System.out.println("---list--lset--------------------------------------------");
        String user = jedis.lset("list_users", 1, "fuckwangxiaojuan");

        for (int i = 0; i < 10; i++) {

            user = jedis.lpop("list_users");
            System.out.println(user);
            if (user != null && !user.equals("")) {

            } else
                break;
        }
    }

    @Test
    public void test21_hash_hmset() {
        System.out.println("---hash--hmset--------------------------------------------");
        Map<String, String> map = new HashMap<String, String>();
        map.put("fangming", "fsmflying@163.com");
        map.put("wangxiaojuan", "wangxiaojuan@163.com");
        map.put("yanxia", "yanxia@163.com");
        map.put("zhangyang", "zhangyang@163.com");
        String user = jedis.hmset("hashmap", map);

    }

    @Test
    public void test22_hash_hgetall() {
        System.out.println("---hash--hgetall--------------------------------------------");
        Map<String, String> map = jedis.hgetAll("hashmap");
        for (String key : map.keySet()) {
            System.out.println("{" + key + ":" + map.get(key) + "}");
        }
        // jedis.keys("*".getBytes());
    }

    @Test
    public void test32_keys() {
        System.out.println("---test32_keys----------------------------------------------");
        Set<String> keys = jedis.keys("*");
        for (String k : keys) {
            System.out.println(k);
//			jedis.get(k)
        }
    }

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
//    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Object lockObject = new Object();

    private static Integer counter = 0;

    public static boolean tryGetDistributedLock(Jedis jedis, int expireTime) {
        String result = jedis.set("dlock01", "1", SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

    static class RedisTool {

        private static final String LOCK_SUCCESS = "OK";
        private static final String SET_IF_NOT_EXIST = "NX";
        private static final String SET_WITH_EXPIRE_TIME = "PX";

        /**
         * 尝试获取分布式锁
         *
         * @param jedis      Redis客户端
         * @param lockKey    锁
         * @param requestId  请求标识
         * @param expireTime 超期时间
         * @return 是否获取成功
         */
        public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
            String result = jedis.set("ds01", "1", SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 1);

            if (LOCK_SUCCESS.equals(result)) {
                return true;
            }
            return false;

        }

    }


    static class TestThread extends Thread {
        /**
         * 最大数量
         */
        public static final int MAX_COUNT = 100;

        JedisPool jedisPool;
        String flag;

        public TestThread(JedisPool pool, String flag) {
            this.jedisPool = pool;
            this.flag = flag;
        }

        @Override
        public void run() {
            for (; counter < MAX_COUNT; ) {
                Jedis localJedis = jedisPool.getResource();
                try {
                    String result = localJedis.set("ds01", "1", SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, 1);
                    if ("OK".equals(result)) {
                        System.out.println(counter + ":" + flag);
                        counter++;
                    }
                } finally {
//                    jedisPool.returnResource(localJedis);
                    localJedis.close();
                }
            }
        }
    }

    /**
     * 多线程应该使用线程池
     *
     * @throws InterruptedException
     */
    @Test
    public void test42_locks() throws InterruptedException {


        Thread t1 = new TestThread(jedisPool, "1");
        Thread t2 = new TestThread(jedisPool, "2");
        Thread t3 = new TestThread(jedisPool, "3");


        t1.start();
        t2.start();
        t3.start();

        for (; counter < TestThread.MAX_COUNT; ) {
            try {
                System.out.println("[main thread]:" + counter);
                Thread.currentThread().sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
//        Thread.currentThread().setDaemon(true);
//        synchronized (lockObject) {
//            lockObject.wait();
//        }
//        Thread.currentThread().wait();
    }
}
