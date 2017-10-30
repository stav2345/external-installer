package main;

import java.io.File;
import java.io.IOException;

import config.PropertiesReader;

public class AppLauncherDelegate {

	/**
	 * Launch the real application
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public void launchApp() throws InterruptedException, IOException {
		
		// read jar relative path
		String appPath = PropertiesReader.getApplicationFolder();
		String jarName = PropertiesReader.getValue(PropertiesReader.JAR_PATH);
		String javaPath = PropertiesReader.getValue(PropertiesReader.JAVA_PATH);
		
		System.out.println("Launching " + jarName 
				+ " with java in " + javaPath 
				+ " from folder " + appPath);
		
		// start the jar
		ProcessBuilder pb = new ProcessBuilder(javaPath, "-jar", jarName);
		
		// set the new working directory
		pb.directory(new File(appPath));
		
		// inherit console
		pb.inheritIO();
		
		// start the process
		Process p = null;
		
		p = pb.start();
		
		p.waitFor();
	}
}
