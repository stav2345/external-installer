package release;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.eclipsesource.json.Json;
import com.eclipsesource.json.JsonArray;
import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonValue;

import config.GithubChecker;

public class ReleaseParser {

	private String jsonInput;
	private JsonValue value;
	
	public ReleaseParser(String jsonInput) {
		this.jsonInput = jsonInput;
	}
	
	private JsonValue getJson() {
		
		if (this.value == null) {
			this.value = Json.parse(jsonInput);
		}
		
		return this.value;
	}
	
	/**
	 * Get the version of the release
	 * @return
	 */
	public String getVersion() {
		JsonValue value = getJson();
		return value.asObject().getString("tag_name", GithubChecker.DEFAULT_VERSION);  // default first version
	}
	
	/**
	 * Get the url from which it can be downloaded the last release
	 * @return
	 * @throws IOException 
	 */
	public String getDownloadUrl() throws IOException {

		// get assets
		JsonObject asset = GetAppRelease();
		
		String url = asset.getString("browser_download_url", "");
		
		return url;
	}
	
	
	public JsonObject getFirstAsset() {
		
		JsonArray assets = getAssets();
		
		JsonObject first = null;
		
		// get first element of the assets
		if (!assets.isEmpty()) {
			first = assets.get(0).asObject();
		}
		
		return first;
	}
	
	/**
	 * Get the release which does not contain the
	 * java folder in it (it is lighter)
	 * @return
	 */
	public JsonObject GetAppRelease() {
		
		JsonArray assets = getAssets();
		for (JsonValue value : assets) {
			String assetName = value.asObject().getString("name", "");
			if (assetName.contains("onlyapp")) {
				return value.asObject();
			}
		}
		
		System.err.println("Cannot find application version with nojava flag in its name. Example tool-nojava.zip");
		System.err.println("Using first available asset instead");
		
		return getFirstAsset();
	}
	
	public JsonArray getAssets() {
		
		JsonValue value = getJson();
		
		// get assets
		JsonArray assets = value.asObject().get("assets").asArray();
		
		return assets;
	}
	
	/**
	 * Get the size of the attachment
	 * @return
	 */
	public int getSize() {
		
		// get assets
		JsonObject app = GetAppRelease();
		if (app == null) {
			return -1;
		}
		
		int size = app.getInt("size", -1);
		
		return size;
	}
	
	/**
	 * Test of json parser
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		String content = readFile("C:\\Users\\avonva\\Desktop\\EclipseWS\\GithubReleaseChecker\\latest.txt");
		ReleaseParser parser = new ReleaseParser(content);
		String version = parser.getVersion();
		System.out.println(version);
	}
	
	public static String readFile(String fileName) throws IOException {
	    BufferedReader br = new BufferedReader(new FileReader(fileName));
	    try {
	        StringBuilder sb = new StringBuilder();
	        String line = br.readLine();

	        while (line != null) {
	            sb.append(line);
	            sb.append("\n");
	            line = br.readLine();
	        }
	        return sb.toString();
	    } finally {
	        br.close();
	    }
	}
}
