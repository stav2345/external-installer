package config;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class to read an xml used to store the properties
 * @author avonva
 *
 */
public class Config {
	
	private String PROXY_CONFIG_PATH = "config/proxyConfig.xml";
	public static final String PROXY_HOST_NAME = "Proxy.ManualHostName";
	public static final String PROXY_PORT = "Proxy.ManualPort";
	public static final String PROXY_MODE = "Proxy.Mode";
	
	private static final String CONFIG_PATH = "config/githubConfig.xml";
	
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
	 * Set the path where the config folder is present, by the default is current directory
	 * @param basePath
	 */
	public Config(String basePath) {
		this.PROXY_CONFIG_PATH = basePath + PROXY_CONFIG_PATH;
	}

	public Config() {}
	
	public String getConfigPath() {
		return this.PROXY_CONFIG_PATH;
	}
	
	/**
	 * Get customized proxy hostname
	 * @return
	 */
	public String getProxyHostname() {
		return getValue(PROXY_CONFIG_PATH, PROXY_HOST_NAME);
	}
	
	/**
	 * Get customized proxy port
	 * @return
	 */
	public String getProxyPort() {
		return getValue(PROXY_CONFIG_PATH, PROXY_PORT);
	}
	
	public ProxyMode getProxyMode() {
		return ProxyMode.fromString(getValue(PROXY_CONFIG_PATH, PROXY_MODE));
	}
	
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
	
	public String getApplicationFolder() {
		String appFolder = getValue(APP_FOLDER);
		return appFolder + System.getProperty("file.separator");
	}
	
	public String getJarPath() {
		String appFolder = getApplicationFolder();
		return appFolder + getValue(JAR_PATH);
	}
	
	/**
	 * Get the real application icon
	 * @return
	 */
	public String getApplicationIconPath() {
		String appFolder = getValue(APP_FOLDER) + System.getProperty("file.separator");
		String iconFolder = getValue(APP_ICON_FOLDER) + System.getProperty("file.separator");
		return appFolder + iconFolder + getAppConfigEntry(APP_ICON);
	}
	
	private String getAppConfigEntry(String appEntry) {
		String appFolder = getValue(APP_FOLDER);
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

		File file = new File(filename);
		System.out.println("Reading property from " + file.getAbsolutePath());
		
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
		
		if ( prop == null )
			return "not found";
		
		String value = prop.getProperty(property);
		
		return value;
	}
}
