package com.fsmflying.guava;

import java.io.BufferedReader;
//import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
//import java.util.Objects;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.google.common.collect.ComparisonChain;

public class Guava01Tests {

	Map<String, String> userMap;

	@Before
	public void before() throws IOException {
		// BufferedReader reader = new BufferedReader(new
		// FileReader("d:\\private\\data.100.2.txt"));
		// userMap = new HashMap<String, String>();
		// String line = null;
		// while ((line = reader.readLine()) != null) {
		// userMap.put(line.split(" ")[0], line.split(" ")[2]);
		// }
		// reader.close();
	}

	@Test
	public void test_01() throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader("d:\\private\\data.100.2.txt"));
		Map<String, String> userMap = new HashMap<String, String>();
		String line;
		while ((line = reader.readLine()) != null) {
			userMap.put(line.split(" ")[0], line.split(" ")[2]);
		}
		reader.close();
	}

	@Test
	public void test_02_Optional() throws IOException {
		Optional<Integer> a = Optional.of(5);
		Optional<Integer> b = Optional.absent();
		Optional<Integer> c = Optional.fromNullable(null);
		System.out.println("a.isPresent():" + a.isPresent());
		System.out.println("a.get():" + a.get());
		System.out.println("b.isPresent():" + b.isPresent());
		System.out.println("b.get():" + (b.isPresent() ? b.get() : null));
		System.out.println("c.isPresent():" + c.isPresent());
		System.out.println("c.get():" + (c.isPresent() ? c.get() : null));
		System.out.println("c.or(99):" + c.or(99));
		System.out.println(a.asSet());

	}

	@Test
	public void test_03_asSet() throws IOException {
		Optional<Integer> a = Optional.of(5);

	}

	@Test
	public void test_04_preconditions() throws IOException {
		Optional<Integer> a = Optional.of(5);
		Preconditions.checkArgument(a.get() < 9);

	}

	@Test
	public void test_05_objects() throws IOException {
		// Optional<Integer> a = Optional.of(5);
		// Preconditions.checkArgument(a.get() < 9);
		// System.out.println(Objects.equals("a", "a"));
		// System.out.println(Objects.equals("a", "b"));
		// System.out.println(Objects.equals("a", null));
		// System.out.println(Objects.equals(null, "a"));
		// System.out.println(Objects.equals(null, null));
		//
		// System.out.println(Objects.hash("a", "b", "c"));
		// System.out.println(Objects.hash("b", "c", "a"));
		// System.out.println(Objects.hash("c", "a", "b"));
		// System.out.println(Ojbects.);
		// Objects.toStringHelper("a").toString();

	}

	static class Person implements Comparable<Person> {
		private String lastName;
		private String firstName;
		private int zipCode;

		public int compareTo(Person other) {
			int cmp = lastName.compareTo(other.lastName);
			if (cmp != 0) {
				return cmp;
			}
			cmp = firstName.compareTo(other.firstName);
			if (cmp != 0) {
				return cmp;
			}
			return Integer.compare(zipCode, other.zipCode);
		}
	}

	@Test
	public void test_06_comparable() throws IOException {
//		 int a = ComparisonChain.start()
//		 .compare(this.aString, that.aString)
//		 .compare(this.anInt, that.anInt)
//		 .compare(this.anEnum, that.anEnum, Ordering.natural().nullsLast())
//		 .result();
	}

	@Test
	public void test_07_comparisionChain() throws IOException {
	
		// int a = ComparisonChain.start()
		// .compare(this.aString, that.aString)
		// .compare(this.anInt, that.anInt)
		// .compare(this.anEnum, that.anEnum, Ordering.natural().nullsLast())
		// .result();
	}

}
