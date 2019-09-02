package main;

import javax.swing.JOptionPane;

import config.GithubChecker;
import dialog.Warning;

public class GeneralLauncherMain {

	public static void main(String[] args) {

		try {
			// initialise the library
			GithubChecker.initialize();
			
			FlowActions actions = new FlowActions();
			actions.start();
		}
		catch (Exception e) {
			e.printStackTrace();
		    String trace = Warning.getStackTrace(e);
		    
		    Warning.warnUser("Generic error", 
		    		"XERRX: Generic runtime error. Please contact zoonoses_support@efsa.europa.eu. Error message " 
		    				+ trace, JOptionPane.ERROR_MESSAGE);
		}
	}
}
