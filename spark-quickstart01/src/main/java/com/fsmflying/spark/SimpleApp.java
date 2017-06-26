package com.fsmflying.spark;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import scala.Tuple2;

public class SimpleApp {
	private static final Pattern SPACE = Pattern.compile(" ");

	public static void main(String[] args) {
		String inputFile = "file:///C:/Users/FangMing/Desktop/news01.txt"; // Should
																			// be
																			// some
																			// file
																			// on
																			// your
																			// system
		SparkConf conf = new SparkConf();
		conf.setMaster("local");
		conf.setAppName("Simple App");
//		JavaSparkContext spark = new JavaSparkContext("local", "Simple App");
		JavaSparkContext spark = new JavaSparkContext(conf);
		JavaRDD<String> lines = spark.textFile(inputFile).cache();

		JavaRDD<String> words = lines.flatMap(new FlatMapFunction<String, String>() {
			@Override
			public Iterator<String> call(String v1) throws Exception {
				return Arrays.asList(SPACE.split(v1.replace("\"", ""))).iterator();
			}
		});

		JavaPairRDD<String, Integer> ones = words.mapToPair(new PairFunction<String, String, Integer>() {
			public Tuple2<String, Integer> call(String s) {
				return new Tuple2<>(s, 1);
			}
		});

		JavaPairRDD<String, Integer> counts = ones.reduceByKey(new Function2<Integer, Integer, Integer>() {
			public Integer call(Integer i1, Integer i2) {
				return i1 + i2;
			}
		});
		
		counts.saveAsTextFile("file:///C:/Users/FangMing/Desktop/news01.out.txt");

		List<Tuple2<String, Integer>> output = counts.collect();
		for (Tuple2<?, ?> tuple : output) {
			System.out.println(tuple._1() + ": " + tuple._2());
		}
		spark.stop();

		// long numAs = logData.filter(new Function<String, Boolean>() {
		// public Boolean call(String s) { return s.contains("a"); }
		// }).count();
		//
		// long numBs = logData.filter(new Function<String, Boolean>() {
		// public Boolean call(String s) { return s.contains("b"); }
		// }).count();

//		System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);
	}
}
