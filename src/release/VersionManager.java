package release;

import java.io.File;
import java.io.IOException;

import config.GithubChecker;
import config.PropertiesReader;
import dialog.ProgressDialog;
import zip.ZipManager;

/**
 * Manage the application versions and updates.
 * @author avonva
 *
 */
public class VersionManager {

	private final static String INSTALL_OK_FILE = "install.ok";
	private String lastRelease;
	private ReleaseParser parser;

	/**
	 * Download the last release of the application from github
	 * @return
	 * @throws IOException
	 */
	public String getLastRelease() throws IOException {
		
		if (lastRelease == null) {
			String username = PropertiesReader.getValue(PropertiesReader.REPOSITORY_OWNER);
			String repo = PropertiesReader.getValue(PropertiesReader.REPOSITORY_NAME);

			GetLastRelease req = new GetLastRelease(username, repo);
			lastRelease = req.get();
		}
		
		return lastRelease;
	}
	
	/**
	 * Get an instance of the release parser to get the release
	 * information
	 * @return
	 * @throws IOException
	 */
	public ReleaseParser getParserInstance() throws IOException {
		
		if (parser == null) {
			
			String lastRelease = getLastRelease();
	
			// check if user has last version
			this.parser = new ReleaseParser(lastRelease);
		}
		
		return parser;
	}
	
	/**
	 * Check if a correct installation of the application is present
	 * into the application folder
	 * @return
	 */
	public boolean isApplicationInstalled() {
		File file = getInstallOkFile();
		return file.exists();
	}
	
	private File getInstallOkFile() {
		String folder = PropertiesReader.getApplicationFolder();
		String filePath = folder + INSTALL_OK_FILE;
		File file = new File(filePath);
		return file;
	}
	
	/**
	 * Check if the user has the latest version or not
	 * @return
	 * @throws IOException
	 */
	public boolean isOldVersion() throws IOException {

		String currentVersion = PropertiesReader.getApplicationVersion();

		// check if user has last version
		ReleaseParser parser = getParserInstance();

		String latestVersion = parser.getVersion();

		boolean isOld = currentVersion.compareTo(latestVersion) < 0;
		
		if (isOld) {
			System.out.println("Need update " + currentVersion 
				+ " => " + latestVersion);
		}
		
		// if we have a older version ask for downloading the last
		return isOld;
	}
	
	/**
	 * Download the last release
	 * @return filename of the file containing the last release
	 * @throws IOException
	 */
	private String downloadLastRelease() throws IOException {
		
		ReleaseParser parser = getParserInstance();
		String endpoint = parser.getDownloadUrl();
		int contentLength = parser.getSize();
		
		// outputfile in temp folder
		String attachmentFilename = GithubChecker.newTempFile("zip");
		
		// start the download process in background
		try(FileDownloader downloader = new FileDownloader(endpoint, 
				contentLength, attachmentFilename)) {
			
			ProgressDialog dialog = new ProgressDialog();
			
			downloader.setProgressBar(dialog.getProgressBar());
			
			// close the dialog at the end
			downloader.setSwingWorkerFinishedListener(new SwingWorkerFinishedListener() {
				
				@Override
				public void finished() {
					dialog.close();
				}
			});
			
			downloader.execute();
			
			while(!downloader.isFinished()) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		return attachmentFilename;
	}
	
	/**
	 * Download last release and replace the old with the new
	 * @throws IOException
	 */
	public void updateVersion() throws IOException {
		
		// download zip of the release
		String newVersionFilename = this.downloadLastRelease();
		String unzippedFolder = newVersionFilename + "_unzipped";
		
		// extract the zip file
		ZipManager.extractFolder(newVersionFilename, unzippedFolder);
		
		// move all the old version files into the oldReleases folder
		OldVersionTracker tracker = new OldVersionTracker();
		tracker.moveOldFiles();
		tracker.moveNewFiles(unzippedFolder);
		
		createInstallOkFile();
	}
	
	/**
	 * Create a new ok file meaning that the installation
	 * was successful
	 * @throws IOException
	 */
	public void createInstallOkFile() throws IOException {
		File file = getInstallOkFile();
		file.createNewFile();
	}
}
