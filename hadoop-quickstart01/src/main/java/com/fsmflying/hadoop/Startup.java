package com.fsmflying.hadoop;

import java.security.NoSuchAlgorithmException;
import java.security.Provider.Service;
import java.util.Set;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

public class Startup {

	private static final String SHUFFLE_KEYGEN_ALGORITHM = "HmacSHA1";
	private static final int SHUFFLE_KEY_LENGTH = 64;

	@SuppressWarnings("restriction")
	public static void main(String[] args) throws NoSuchAlgorithmException {
//		System.out.println("Hello World!");

//		printSecurityProviders();

		testKeyGenerator();
	}
	
	public static void printSecurityProviders()
	{
//		java.security.
		java.security.Provider[] providers = java.security.Security.getProviders();
		for(java.security.Provider p: providers)
		{
			System.out.println("-------------------------------------------------------");
			System.out.println("######[Provider Name]:"+p.getName()+"##################");
			Set<Service> services = p.getServices();
			for(Service s:services)
			{
				System.out.println("[Algorithm]["+s.getAlgorithm()+"]:"+s.getClassName());
			}
//			System.out.println("[Provider Class]"+p.getServices());
		}
	}
	
	public static void testKeyGenerator() throws NoSuchAlgorithmException
	{
//		java.security.Security.addProvider(new com.sun.crypto.provider.SunJCE());
//		Mac mac = Mac.getInstance("HmacSHA1");
//		mac.getProvider().getName();
		KeyGenerator keyGen = KeyGenerator.getInstance("HmacMD5");//HmacSHA1
		System.out.println(keyGen.getProvider().getName());
//		keyGen.init(64);
//		SecretKey shuffleKey = keyGen.generateKey();
//		System.out.println(shuffleKey.toString());
	}
}
