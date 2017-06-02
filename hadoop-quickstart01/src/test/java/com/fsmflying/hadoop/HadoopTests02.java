package com.fsmflying.hadoop;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.After;
import org.junit.Before;

public class HadoopTests02 {
	
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

		// the way 1 to create
		// System.setProperty("HADOOP_USER_NAME", "hadoop");
		// conf.set("fs.defaultFS", "hdfs://master.hadoop:9000");
		// fileSystem = FileSystem.get(conf);

		// the way 2 to create
		fileSystem = FileSystem.get(new URI("hdfs://master.hadoop:9000"), conf, "hadoop");

		// System.setProperty("HADOOP_USER_NAME", "hadoop");
	}

	@After
	public void after() throws IOException {
		this.fileSystem.close();
	}
	
	public void test00()
	{
//		StringTokenizer itr = new StringTokenizer(value.toString());
//	      while (itr.hasMoreTokens()) {
//	        word.set(itr.nextToken());
//	        context.write(word, one);
//	      }
	}

}
