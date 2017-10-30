package main;

import java.io.IOException;

import javax.swing.JOptionPane;

import release.VersionManager;

public class FlowActions {

	private VersionManager check;
	
	public FlowActions() {
		check = new VersionManager();
	}
	
	/**
	 * Start the update process
	 */
	public void start() {

		// if no application is found install the latest version 
		// and launch it
		if (!check.isApplicationInstalled()) {
			boolean installed = installLatestVersion();
			
			if (installed)
				launchApp();
			return;
		}

		// if there is a working application
		boolean isOld = false;
		try {
			isOld = check.isOldVersion();
		} catch (IOException e) {
			e.printStackTrace();
			launchApp();
			return;
		}

		// if last version is already used
		if (!isOld) {
			launchApp();
			return;
		}
		
		// ask user for installing
		boolean installApp = askConfirmationForInstall();

		// if no is chosen, open standard app
		if (!installApp) {
			launchApp();
			return;
		}
		
		// otherwise install the latest version
		// and then launch it
		boolean installed = installLatestVersion();
		
		if (installed)
			launchApp();
	}
	
	/**
	 * Do you want to install the latest version?
	 * @return
	 */
	private boolean askConfirmationForInstall() {
		
		// ask for installing the new version
		// otherwise ask for downloading it
		int val = JOptionPane.showConfirmDialog(null, 
				"An update of the tool is available. Do you want to download it?", 
				"Update needed!",
				JOptionPane.INFORMATION_MESSAGE | JOptionPane.YES_NO_OPTION);
		
		return val == JOptionPane.YES_OPTION;
	}
	
	/**
	 * Launch the application
	 */
	private void launchApp() {
		
		AppLauncherDelegate launcher = new AppLauncherDelegate();
		try {
			
			launcher.launchApp();
			
		} catch (InterruptedException | IOException e) {
			
			e.printStackTrace();
			
			JOptionPane.showMessageDialog(null, "Cannot launch the application. Error message " + e.getMessage(), 
					"Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private void showConnectionError() {
		
		JOptionPane.showMessageDialog(null, "ERROR: Cannot download the latest version of the tool. Check your connection.", 
				"Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Install the latest version of the application
	 * @return
	 */
	private boolean installLatestVersion() {
		try {
			check.updateVersion();
			
		} catch (IOException e) {

			e.printStackTrace();

			showConnectionError();
			
			return false;
		}
		
		return true;
	}
}
