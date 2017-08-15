package com.fsmflying.common.classloader;

public class Startup {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(System.getProperty("sun.boot.class.path"));//根类加载器
		System.out.println(System.getProperty("java.ext.dirs"));//扩展类加载器
		System.out.println(System.getProperty("java.class.path"));//应用类加载器
	}

}
