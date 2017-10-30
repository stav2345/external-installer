package main;

import config.GithubChecker;

public class StartUI {

	public static void main(String[] args) {

		// initialize the library
		GithubChecker.initialize();
		
		FlowActions actions = new FlowActions();
		actions.start();
	}
}
