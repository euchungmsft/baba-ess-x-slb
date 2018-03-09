package com.eg.baba.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ClientProfile{

	private static final Properties config = new Properties();

	static {
		try {
			load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Shutting down");
			System.exit(-1);
		}
	}
	
	private static void load() throws FileNotFoundException, IOException {
		// TODO Auto-generated method stub
		
		File file = null;
		if(! (file = new File(".clientprofile")).exists())
			if(! (file = new File(System.getProperty("user.home")+File.separator+".clientprofile")).exists())
				throw new IOException("client profile was not found from either of current path and user home");
		
		FileInputStream in = new FileInputStream(file);
		config.load(in);
		in.close();
		
	}

	public static String getKeyId() {
		// TODO Auto-generated method stub
		return getConfig("key.id", null);
	}

	public static String getKeySecret() {
		// TODO Auto-generated method stub
		return getConfig("key.secret", null);
	}

	public static String getEndpoint() {
		// TODO Auto-generated method stub
		return getConfig("endpoint", null);
	}

	public static String getConfig(String string) {
		// TODO Auto-generated method stub
		return getConfig(string, null);
	}

	public static String getConfig(String string, String deflt) {
		// TODO Auto-generated method stub
		
		String value;
		
		value = config.getProperty(string, deflt);
		if(value == null || (deflt != null && deflt.trim().length() <= 0))
			value = deflt;
		else
			value = value.trim();
		
		//System.out.format("Parameter> %s=%s\n", string, value);
		
		String value1 = System.getProperty(string, deflt);
		if(value1 == null || (deflt != null && deflt.trim().length() <= 0))
			value1 = deflt;
		else
			value1 = value1.trim();
		
		if(value1 != null)
			value = value1;
		
		//System.out.format("Parameter> %s=%s\n", string, value);
		
		return value;
		
	}


	public static void printObject(Object src) {
		// TODO Auto-generated method stub

		printObject(src, true);
	}

	public static void printObject(Object src, boolean pretty) {
		// TODO Auto-generated method stub
		
		Gson gson = pretty ? new GsonBuilder().setPrettyPrinting().create() : new GsonBuilder().create();
		gson.toJson(src, System.out);
	}

}
