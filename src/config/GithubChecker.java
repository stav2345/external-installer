package config;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

/**
 * General settings and start up checks
 * @author shahaal
 * @author avonva
 *
 */
public class GithubChecker {
	
	// template folder name
	public static final String TEMP_FOLDER_NAME = "temp";
	// default version used if no version is found
	public static final String DEFAULT_VERSION = "0.0.0";
	
	/**
	 * return the temp folder path in AppData
	 * @author shahaal
	 * @return
	 */
	public static String getTempFolder() {
		return GithubConfig.getCbFolder() 
				+ System.getProperty("file.separator")+TEMP_FOLDER_NAME 
				+ System.getProperty("file.separator");
	}
	
	/**
	 * Check if a directory exists. If not create it.
	 * @param dirName
	 */
	public static void checkDir(String dirName) {
		
		// create temporary folder if needed
		File file = new File(dirName);
		
		System.out.println("Creating " + dirName);
		
		// create the directory
		if (!file.exists()) {
			boolean created = file.mkdir();
			
			if (!created) {
				System.err.println("Cannot create "+dirName+" directory");
				return;
			}
		}
	}
	
	/**
	 * check the esistence of the app and the temp folder
	 * @author shahaal
	 */
	public static void initialize() {
		// check app and temp folder
		checkDir(GithubConfig.getCbFolder());
		checkDir(getTempFolder());
	}
	
	/**
	 * Delete all the temporary files
	 * @throws IOException
	 */
	public static void clearTemp() throws IOException {
		File src = new File(getTempFolder());
		for (File file : src.listFiles())
			FileUtils.forceDelete(file);
	}
	
	public static String newTempFile(String format) {
		return getTempFolder() + "temp_" + System.currentTimeMillis() + "." + format;
	}
}
