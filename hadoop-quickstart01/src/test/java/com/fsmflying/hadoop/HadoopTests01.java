package com.fsmflying.hadoop;

import org.junit.Before;
import org.junit.After;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;

//import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
//import org.apache.hadoop.hdfs.client.HdfsDataOutputStream;

public class HadoopTests01 {

	FileSystem fileSystem = null;
	String baseDir = null;
	String projectDir = null;

	@Before
	public void before() throws IOException, InterruptedException, URISyntaxException {

		baseDir = this.getClass().getResource("/").getPath();
		projectDir = baseDir.substring(0, baseDir.indexOf("/target/"));
		System.out.println("baseDir=" + baseDir);
		System.out.println("projectDir=" + projectDir);

		Configuration conf = new Configuration();
        //
//		 the way 1 ：　使用classpath下的core-site.xml文件来配置hdfs的连接信息
		 System.setProperty("HADOOP_USER_NAME", "hadoop");
		 fileSystem = FileSystem.get(conf);

		 //the way 2:设置配置属性来定义连接信息
//		 conf.set("fs.defaultFS", "hdfs://master.hadoop:9000");
//		 fileSystem = FileSystem.get(conf);

		// the way 3 :直接配置
//		fileSystem = FileSystem.get(new URI("hdfs://192.168.1.131:9000"), conf, "hadoop");

		// System.setProperty("HADOOP_USER_NAME", "hadoop");
	}

	@After
	public void after() throws IOException {
		this.fileSystem.close();
	}

	@Test
	public void test00_help() {
//		System.out.println(this.getClass().getResource("/").getPath());
//		System.out.println("##environments##########################################################");
//		Map<String, String> envs = System.getenv();
//		for (String key : envs.keySet()) {
//			System.out.println("--------------------------------------------");
//			System.out.println(key + "=" + envs.get(key));
//		}

//		System.out.println("##properties##########################################################");
//		Properties props = System.getProperties();
//		for (Object key : props.keySet()) {
//			System.out.println("--------------------------------------------");
//			System.out.println(key + "=" + envs.get(key));
//		}
		
//		StringTokenizer itr = new StringTokenizer(value.toString());
//	      while (itr.hasMoreTokens()) {
//	        word.set(itr.nextToken());
//	        context.write(word, one);
//	      }

	}

	@Test
	public void test01_copyFileToLocal() throws IllegalArgumentException, IOException {

		InputStream in = fileSystem.open(new Path("/fruit01.txt"));

		String outFileName = this.getClass().getResource("/").getPath() + "fruit01.txt";
		OutputStream out = new FileOutputStream(outFileName);
		IOUtils.copyBytes(in, out, 4096, true);

		/*
		 * org.apache.commons.compress.utils.IOUtils.copy(in, System.out);
		 * in.close(); out.close();
		 */
		System.out.println("copy file to location:" + outFileName);
	}

	@Test
	public void test01_copyFileToLocal_02() throws IllegalArgumentException, IOException {
		boolean delSrc = true;
		String src = "/test/pom.xml";
		String dst = baseDir + "/downloads/"
				+ new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime()) 
				+ "/pom.xml";
		boolean useRawLocalFileSystem = true; // 是否使用crc-32校验
		fileSystem.copyToLocalFile(new Path(src), new Path(dst));
//		fileSystem.copyToLocalFile(delSrc, new Path(src), new Path(dst));
//		fileSystem.copyToLocalFile(delSrc, new Path(src), new Path(dst), useRawLocalFileSystem);
		System.out.println("copy file to location:" + dst);
	}

	@Test
	public void test02_displayFileToStdout() throws IllegalArgumentException, IOException {
		InputStream in = fileSystem.open(new Path("/fruit01.txt"));
		// IOUtils.copy(in, System.out);

		IOUtils.copyBytes(in, System.out, 4096, true);

		// org.apache.commons.compress.utils.IOUtils.copy(in, System.out);
		// in.close();
	}

	@Test
	public void test03_uploadFileToHDFS() throws IllegalArgumentException, IOException {
		String fileName = "/pom.xml";
		// 如果存在，则先删除
		if (fileSystem.exists(new Path(fileName)) && !fileSystem.delete(new Path(fileName), true)) {
			System.out.println("The file(" + fileName + ") does not exists ");
			return;
		}
		String inFileName = projectDir + "/pom.xml";
		InputStream in = new FileInputStream(inFileName);
		FSDataOutputStream out = fileSystem.create(new Path(fileName));
		IOUtils.copyBytes(in, out, 4096, true);
		/*
		 * org.apache.commons.compress.utils.IOUtils.copy(in, out); in.close();
		 * out.close();
		 */
		System.out.println("The file(" + fileName + ") was uploaded sucessfully! ");
	}

	@Test
	public void test04_deleteFile() throws IllegalArgumentException, IOException {

		String fileName = "/pom.xml";
		if (fileSystem.exists(new Path(fileName))) {
			if (fileSystem.delete(new Path(fileName), true))
				System.out.println("The file(" + fileName + ") was deleted sucessfully! ");
			else
				System.out.println("The file(" + fileName + ") was deleted unsucessfully! ");

		} else {
			System.out.println("The file(" + fileName + ") does not exists ");
		}
	}

	@Test
	public void test11_mkdir() throws IllegalArgumentException, IOException {

		String path = "/eclipse/hadoop-quickstart01";
		if (fileSystem.exists(new Path(path))) {
			System.out.println("The path(" + path + ") does exists! and need to delete it ! ");
			if (fileSystem.delete(new Path(path), true))
				System.out.println("The path(" + path + ") does deleted sucessfully! ");
			else
				System.out.println("The path(" + path + ") was deleted unsucessfully! ");
		}

		if (fileSystem.mkdirs(new Path(path))) {
			System.out.println("The path(" + path + ") was created sucessfully! ");
		} else {
			System.out.println("The path(" + path + ") was created unsucessfully! ");
		}

	}

	@Test
	public void test12_mkdir() throws IllegalArgumentException, IOException {

		String path = "/eclipse/hadoop-quickstart01";
		if (fileSystem.exists(new Path(path))) {
			System.out.println("The path(" + path + ") does exists! and need to delete it ! ");
			if (fileSystem.delete(new Path(path), true))
				System.out.println("The path(" + path + ") does deleted sucessfully! ");
			else
				System.out.println("The path(" + path + ") was deleted unsucessfully! ");
		}

		if (fileSystem.mkdirs(new Path(path))) {
			System.out.println("The path(" + path + ") was created sucessfully! ");
		} else {
			System.out.println("The path(" + path + ") was created unsucessfully! ");
		}

	}
}
