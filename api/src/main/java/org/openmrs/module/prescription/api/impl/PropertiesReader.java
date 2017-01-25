package org.openmrs.module.prescription.api.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.io.File;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.openmrs.module.prescription.Drug;

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
			
			System.out.println(prop.getProperty("prescription_file_path"));
			
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
