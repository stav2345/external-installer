package release;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetLastRelease {
	
	private static final String URL_PATTERN = "https://api.github.com/repos/:username/:repo/releases/latest";
	
	private String endpoint;
	
	/**
	 * Get the last release of the repository
	 * @param githubUsername
	 * @param repositoryName
	 */
	public GetLastRelease(String githubUsername, String repositoryName) {
		
		this.endpoint = URL_PATTERN.replace(":username", githubUsername)
				.replace(":repo", repositoryName);
	}
	
	public String get() throws IOException {
		
		System.out.println("Github request to " + endpoint);
		
	    //Create connection
	    URL url = new URL(endpoint);
	    
	    StringBuilder result = new StringBuilder();
	    HttpURLConnection con = (HttpURLConnection) url.openConnection();

	    con.setRequestMethod("GET");
	    con.setRequestProperty("Accept", "application/vnd.github.v3+json");
	    
	    System.out.println("Sending request...");
	    
	    int status = con.getResponseCode();
	    
	    if (status != 200) {
		    System.out.println("Error: response status " + status);
	    	return null;
	    }
	    
	    BufferedReader rd = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    
	    String line;
	    while ((line = rd.readLine()) != null) {
	    	result.append(line);
	    }
	    rd.close();
	    
	    con.disconnect();
	    
	    return result.toString();
	}
}
