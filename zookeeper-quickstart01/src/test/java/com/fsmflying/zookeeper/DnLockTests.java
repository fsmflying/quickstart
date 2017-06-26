package com.fsmflying.zookeeper;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class DnLockTests {
	@Before
	public void before() {

	}

	@After
	public void after() {
	}

	@Test
	public void test_00() {
		DnLock lock = new DnLock("192.168.1.104:2181","aaa");
		
	}
}
