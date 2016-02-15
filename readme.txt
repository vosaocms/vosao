                         --------------------------------
                                    Vosao CMS. 
                         Simple CMS for Google App Engine
                         --------------------------------
           
  Vosao CMS is a simple and easy to use tool for creating sites and hosting them
on Google App Engine. Vosao CMS is developped in Java.
According to Google free quota in most cases you will have free hosting for 
your site. 

1. Prerequisites
=================

	1) You must have a Google App Engine application : see https://cloud.google.com/appengine/

	2) Download and install the GAE SDK for Java : see https://cloud.google.com/appengine/downloads

2. Installing Vosao CMS
==========================

	1) Download the latest Vosao version (vosaocms-x.y.z.war) from Vosao Google Drive.
	   see : 
	   
		   https://drive.google.com/folderview?id=0B5kyNpiXIjiiV0lsa2ZRZHVFRjQ&usp=sharing
	   
	   Vosao Google Group : 
	   
		   https://groups.google.com/forum/#!forum/vosao-cms-development	
		
	2) Unpack vosaocms-x.y.z.war

	3) In the unpacked directory, find the file WEB-INF/appengine-web.xml and change 
       the application name to your appspot application name

	4) Upload the CMS application by issuing the GAE SDK command line:
	   {SDK bin directory}\appcfg.cmd update <unpacked war directory>
	
3. Using Vosao CMS :
======================
	
	Your GAE application site is on http://<applicationname>.appspot.com
	
	The cms to create your site is at 
			http://<applicationname>.appspot.com/cms/ 
														(with end /) 
			
	Please authenticate with user/pwd admin@test.com/admin
	
	To launch default site go to "Configuration" and click "Load default site" 
	
	Default site example : http://monvosao.appspot.com

3. Repository
================

New Vosao repository is on GitHub !

    https://github.com/vosaocms/vosao
    
    
4. Building 
==============

To start development on Vosao CMS you will need to :

	1) Retrieve 'vosaocms' project from GitHub Repository with Eclipse eGit plugin. 
   	   Create a local repository named 'vosao'

	2) Configure "web/ant.properties" and "web/ant-macros" with your GAE SDK directory.

	3) Install and configure Maven and Ant

	4) Launch 'build.bat'. The 'war' is created in "web/target" directory


4. Running locally
====================

	Launch web/run.bat 


Have fun,

  Emilio,
  Vosao development team
  
 
   