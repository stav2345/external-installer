package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to read an xml used to store the properties
 * @author avonva
 *
 */
public class GithubConfig {
	
	private static final String CONFIG_PATH = "config/githubConfig.xml";
	
	private static final String USR_LOCAL_APPDATA_PATH = System.getProperty("user.home") + "\\AppData\\Local\\";
	private static final String CB_FOLDER_NAME = "EFSA_Cataloge_browser";
	private static final String TSE_FOLDER_NAME = "EFSA_TSE";
	public static final String JAVA_PATH = "Application.JavaPath";
	public static final String REPOSITORY_NAME = "Github.RepositoryName";
	public static final String REPOSITORY_OWNER = "Github.RepositoryOwner";
	private static final String APP_FOLDER = "Application.Folder";
	public static final String JAR_PATH = "Application.JarPath";
	public static final String APP_ICON = "Application.IconEntry";
	
	public static final String APP_CONFIG_FILE = "Application.ConfigFile";
	public static final String APP_VERSION_ENTRY = "Application.VersionEntry";
	private static final String APP_ICON_FOLDER = "Application.IconFolder";
	public static final String APP_NAME_ENTRY = "Application.NameEntry";
	public static final String APP_DB_FOLDER = "Application.DatabaseFolder";
	
	public static final String APP_KEYWORD_NAME = "Application.Keyword";
	
	/**
	 * Get the real application version
	 * @return
	 */
	public String getApplicationVersion() {
		String version = getAppConfigEntry(APP_VERSION_ENTRY);
		
		if (version == null || version.isEmpty()) {
			version = GithubChecker.DEFAULT_VERSION;
		}

		return version;
	}
	
	public String getApplicationName() {
		return getAppConfigEntry(APP_NAME_ENTRY);
	}
	
	/**
	 * the method return the Catalogue browser folder 
	 * located into the user appdata
	 * @author shahaal
	 * @return
	 */
	public static String getCbFolder() {
		return USR_LOCAL_APPDATA_PATH+CB_FOLDER_NAME;
	}
	
	/**
	 * the method return the TSE folder 
	 * located into the user appdata
	 * @author shahaal
	 * @return
	 */
	public static String getTseFolder() {
		return USR_LOCAL_APPDATA_PATH+TSE_FOLDER_NAME;
	}
	
	/**
	 * get the cb app folder
	 * @author shahaal
	 * @return
	 */
	public String getCbAppFolder() {
		return getCbFolder()+ System.getProperty("file.separator")+getValue(APP_FOLDER);
	}
	
	/**
	 * get the java path from the executable file location
	 * @return
	 */
	public String getJavaPathDir() {
		return System.getProperty("user.dir") + System.getProperty("file.separator")+getValue(JAVA_PATH);
	}
	
	/**
	 * get the tse app folder
	 * @author shahaal
	 * @return
	 */
	public String getTseAppFolder() {
		return getTseFolder()+ System.getProperty("file.separator")+getValue(APP_FOLDER);
	}
	
	public String getJarPath() {
		return getValue(JAR_PATH);
	}
	
	/**
	 * Get the real application icon
	 * @return
	 */
	public String getApplicationIconPath() {
		String appFolder = getCbFolder() + System.getProperty("file.separator");
		String iconFolder = getValue(APP_ICON_FOLDER) + System.getProperty("file.separator");
		return appFolder + iconFolder + getAppConfigEntry(APP_ICON);
	}
	
	private String getAppConfigEntry(String appEntry) {
		String appFolder = getCbFolder();
		String appConfigFile = getValue(APP_CONFIG_FILE);
		String appConfigEntry = getValue(appEntry);
		
		return getValue(appFolder + System.getProperty("file.separator") 
			+ appConfigFile, appConfigEntry);
	}
	
	/**
	 * Read the application properties from the xml file
	 * @return
	 */
	public Properties getProperties(String filename) {
		
		Properties properties = new Properties();

		try(InputStream stream = new FileInputStream(filename)) {
			properties.loadFromXML(stream);
		}
		catch (IOException e) {
			System.err.println("The " + filename + " file was not found. Please check!");
		}

		return properties;
	}
	
	/**
	 * Get a property value given the key
	 * @param property
	 * @return
	 */
	public String getValue(String property) {
		return getValue(CONFIG_PATH, property);
	}
	
	private String getValue(String propertiesFilename, String property) {
		
		Properties prop = getProperties(propertiesFilename);
		
		if (prop == null)
			return null;
		
		String value = prop.getProperty(property);
		
		return value;
	}
}
