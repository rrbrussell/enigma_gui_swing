/**
 * Copyright (c) 2014 Robert R. Russell
 */
package com.rrbrussell.enigma_gui_swing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;





//import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * @author Robert R. Russell
 * @author robert@rrbrussell.com
 * 
 * The launcher for Swinging Enigma.
 *
 */
public class Main extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -129118185558719896L;
	
	private JPanel simpleEnigma;
	private JPanel complexEnigma;
	
	private static File privateDirectory;
	private static File propertiesFile;
	
	public Main(Properties storedOptions) {
		super("Swinging Enigma");
		String viewToLoad = storedOptions.getProperty("EngimaView");
		String versionToLoad = storedOptions.getProperty("EngimaVersion");
		//TODO modify enigma_demonstration_java to provide an interface
	}
	
	/**
	 * @return 
	 * 
	 */
	public static Properties buildDefaultProperties() {
		/*
		 * TODO check if property arguments on the VM command line are
		 * passed into System.getProperties();
		 */
		Properties defaultOptions = new Properties(System.getProperties());
		defaultOptions.setProperty("EngimaView", "simple");
		defaultOptions.setProperty("EnigmaVersion", "M3");
		return defaultOptions;
	}
	
	/**
	 * @param defs
	 * @return
	 */
	public static Properties loadUserProperties(Properties defs) {
		privateDirectory = new File(System.getProperty("user.home"),
				File.separator + ".swinging_enigma");
		propertiesFile = new File(privateDirectory,
				File.separator + "Main.properties");
		Properties loadedProperties = null;
		
		if(propertiesFile.exists() && propertiesFile.canRead()) {
			loadedProperties = new Properties(defs);
			try {
				Reader propertiesStore = new FileReader(propertiesFile);
				loadedProperties.load(propertiesStore);
				propertiesStore.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			loadedProperties = defs;
		}
		return loadedProperties;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties configuration = Main.buildDefaultProperties();
		configuration = Main.loadUserProperties(configuration);
		Main launched = new Main(configuration);
		
	}

}
