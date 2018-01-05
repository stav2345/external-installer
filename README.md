# Github release checker
This is a library is a wrapper (i.e. it wraps a generic application) which can be used to check if new versions of an application are released (in a github repository). In particular, it is sufficient to configure
the config/githubConfig.xml file to use the library (no technical capabilities needed!).

In particular, it checks if there is a new release each time the application
is launched. If a new release is present, then it asks the user if he/she wants to download the updated software. In that case, the old version is replaced with the 
new one (maintaining some special folders which should not be lost while updating, as the database).

If no version is present while launching the application, the library will install the latest release without asking anything.

## Project structure

		├── app (where the application is downloaded)
		├── config
		|   ├── githubConfig.xml
		├── jre1.8.0_152 (example of JRE which contains java)
		|   ├── bin
		|       └── java.exe
		├── temp (temporary files are stored here)
		|   ├── temp1
		|   └── temp2
		├── launcher.exe (executable which starts the library)


## githubConfig.xml file

* Github.RepositoryName: this is the name of the repository where the library should look to see if there are new releases. Example: catalogue-browser
* Github.RepositoryOwner: this is the username of the owner of the repository. Example: openefsa
* Application.JavaPath: this is the path where java.exe is located (needed to start the application). Example: ..\jre1.8.0_111\bin\java.exe
* Application.Keyword: keyword which will be searched in the new release assets to identify the file which contains the new application. Example: onlyapp-win-64bit
* Application.Folder: name of the folder which will contain the application. Default is app
* Application.ConfigFile: name of the configuration file of the application inside the Application.Folder directory. Example: config\appConfig.xml  This is used
to retrieve the current version of the application.
* Application.JarPath: the application jar which will be executed after the updates checking.
* Application.IconFolder: folder of the icons of the application inside the Application.Folder (if needed)
* Application.NameEntry: the entry which contains the name of the application inside the Application.ConfigFile file
* Application.VersionEntry: the entry which contains the version of the application inside the Application.ConfigFile file
* Application.IconEntry: the entry which contains the filename of the icon of the application inside the Application.ConfigFile file
* Application.DatabaseFolder: the name of the folder inside the Application.Folder which contains the database of the application (this will not be deleted with the new releases!)