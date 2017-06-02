package com.fsmflying.hadoop.mapreduce;

import java.io.IOException;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCount {

	public static Logger logger = LoggerFactory.getLogger(WordCount.class);

	public static class WordCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

		private final static IntWritable one = new IntWritable(1);
		private Text word = new Text();

		public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String strValue = value.toString();
			word.set(strValue.substring(strValue.lastIndexOf('@') + 1).toLowerCase());
			context.write(new Text(word), one);

		}
	}

	public static class IntSumReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
		private IntWritable result = new IntWritable();

		public void reduce(Text key, Iterable<IntWritable> values, Context context)
				throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable val : values) {
				sum += val.get();
			}
			result.set(sum);
			if (sum >= 100) {
				logger.info("{key:" + key + ",sum:" + sum + "}");
			}
			// System.out.println("{key:" + key + ",sum:" + sum + "}");
			context.write(key, result);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration conf = new Configuration();
		// System.setProperty("HADOOP_USER_NAME", "hadoop");

		Job job = Job.getInstance(conf, "word-count");
		job.setJarByClass(WordCount.class);

		job.setMapperClass(WordCountMapper.class);
		job.setCombinerClass(IntSumReducer.class);
		job.setReducerClass(IntSumReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(IntWritable.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		FileInputFormat.addInputPath(job, new Path("/share/short/"));//csdn.part03.txt
		String suffix = new java.text.SimpleDateFormat("yyyyMMdd'T'HHmmss").format(Calendar.getInstance().getTime());
		FileOutputFormat.setOutputPath(job, new Path("/output/" + suffix));
		System.exit(job.waitForCompletion(true) ? 0 : 1);
	}

}
