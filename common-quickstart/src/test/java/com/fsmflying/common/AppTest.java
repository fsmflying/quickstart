package com.fsmflying.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class AppTest {
	@Test
	public void test_01_char() throws IOException {
		// ;
		FileInputStream fis01 = new FileInputStream("d:\\private\\csdn.10000.txt");
		Reader reader01 = new InputStreamReader(fis01);
		long length = 1024, readIndex = 50;
		char[] chs = new char[(int) length];
		char[] appchs = new char[1024];
		reader01.read(chs, 50, (int) length - 50);
		char ch;
		int j = 0;
		while ((ch = (char) reader01.read()) != '\n') {
			appchs[j] = ch;
			// System.out.println(ch);
			j++;
		}
		// System.out.println(new String(chs)+new String(appchs));

		List<String> list = new ArrayList<String>();
		StringBuilder sb = new StringBuilder(new String(chs) + new String(appchs));
		int nextFromIndex= 0;
		int index = sb.indexOf("\n", nextFromIndex);

		while (index != -1) {
			list.add(sb.substring(nextFromIndex, index-1).trim());
			nextFromIndex=index+1;
			index = sb.indexOf("\n", index + 1);
		}
		for (String s : list)
			System.out.println(s);

		fis01.close();
		reader01.close();

	}
	
	@Test
	public void test_02_readToEnd() throws IOException {
		// ;
		FileInputStream fis01 = new FileInputStream("d:\\private\\csdn.10.txt");
		Reader reader01 = new InputStreamReader(fis01);
		
		char ch;
		while ((ch = (char) reader01.read()) != '\0') {
			 System.out.print(ch);
			 ch = (char) reader01.read();
		}
		reader01.close();
		fis01.close();

	}
}
