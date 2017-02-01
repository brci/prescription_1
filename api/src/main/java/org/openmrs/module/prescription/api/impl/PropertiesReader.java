package org.openmrs.module.prescription.api.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesReader {
	
	public Properties readProperties() {
		
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			String filename = "config.properties";
			
			ClassLoader classLoader = getClass().getClassLoader();
			input = classLoader.getResourceAsStream(filename);
			
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return null;
			}
			
			prop.load(input);
			
		}
		catch (IOException ex) {
			ex.printStackTrace();
		}
		finally {
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException e) {
					e.printStackTrace();
					
				}
			}
		}
		return prop;
	}
	
}
