package com.fsmflying.common.thread;

import java.io.Reader;
import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


//import org.junit.Test;

/**
 * 将一个数据文件拆分成多份，静态常量NM指定拆分的数量,自动计算每个文件被拆分后的数量
 * 
 * @author FangMing
 *
 */
public class Thread02ReadFile {

	public static final int NM = 10;
//	public static final String fileName = "d:\\private\\csdn.part01.txt";
	public static final String fileName = "d:\\private\\csdn.txt";
	
	public static void main(String[] args) throws IOException, InterruptedException {

//		Runtime.getRuntime().
//		test();
		//拆成10个文件
		FMSyncFileSplitHandler  handler = new FMSyncFileSplitHandlerForFixedFiles(fileName, 3);
		//拆成每个文件有100000行的一组文件
		long start = System.currentTimeMillis();
//		MySyncFileSplitHandler  handler = new MySyncFileSplitHandlerForFixedRows(fileName, 100000);
		handler.split();
		long end = System.currentTimeMillis();
//		System.out.println("文件总行数：" + rowCount);
		System.out.println("总花费时间(毫秒)：" + (end - start));
	}
	
	public static void test() throws IOException, InterruptedException
	{
		FileInputStream fis01 = new FileInputStream(fileName);

		// 计算原始文件行数
		BufferedInputStream bis01 = new BufferedInputStream(fis01);
		long rowCount = 0;
		byte[] c = new byte[1024 * 1024];
		int readChars = 0;
		long start = System.currentTimeMillis();
		while ((readChars = bis01.read(c)) != -1) {
			for (int i = 0; i < readChars; ++i) {
				if (c[i] == '\n') {
					++rowCount;
				}
			}
		}
		bis01.close();
		long end = System.currentTimeMillis();
		System.out.println("文件总行数：" + rowCount);
		System.out.println("总花费时间(毫秒)：" + (end - start));

		// 计算拆分文件的行数，即每次应该在原始文件中读取的行数
		long pageSize = rowCount / NM + 1;
		System.out.println("PageSize:" + pageSize);

		Reader reader01 = new InputStreamReader(new FileInputStream(fileName));
		BufferedReader reader03 = new BufferedReader(reader01);
		FMSyncFileReader reader04 = new FMSyncFileReader(reader03, fileName);

		ExecutorService es01 = Executors.newCachedThreadPool();
		for (int i = 0; i < NM; i++) {
			es01.submit(new FMFileSplitHandler((int) pageSize, reader04));
		}
		es01.shutdown();
		while (true) {
			System.out.println("=====check======");
			// if (es01.isShutdown()) {
			// System.out.println("===============isShutdown===========================");
			// break;
			// }
			if (es01.isTerminated()) {
				System.out.println("===============isTerminated===========================");
				if (reader01 != null)
					try {
						reader01.close();
						fis01.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
			}
			Thread.sleep(100 * 5);
		}
	}

}










