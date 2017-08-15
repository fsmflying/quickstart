package com.fsmflying.redis01;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;

public class RedisTest01 {

	Jedis jedis = null;

	@Before
	public void before() {
		jedis = new Jedis("master.hadoop", 6379);
	}

	@After
	public void after() {
		jedis.close();
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
		for (String k : keys){
			System.out.println(k);
//			jedis.get(k)
		}
	}
}
