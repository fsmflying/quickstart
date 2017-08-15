package com.fsmflying.common;

import org.junit.Test;

import com.alibaba.druid.filter.config.ConfigTools;

public class DruidTests {

	@Test
	public void test_01_encrypt() {
		String originalText = "fangming";
		String encryptedText;
		try {
			encryptedText = ConfigTools.encrypt(originalText);
			System.out.println(originalText + ":" + encryptedText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void test_02_decrypt() {
		String originalText;
		String encryptedText = "BWU5dcFaNV0zmVumbVGs5v2wcnkyl4ZJbpxV5Ev1A+Hjs2UI1gqYPe1xlNN5AjW0Yb0BqG9XRALpql3nxbNyZw==";
		try {
			originalText = ConfigTools.decrypt(encryptedText);
			System.out.println(encryptedText + ":" + originalText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
