# Vosao

Vosao CMS is a simple and easy to use tool for creating sites and hosting them on the Google App Engine of the Google Cloud. 

Vosao CMS is developped in Java & Javascript ans is a complete Web 2.0 application. According to Google free quota you will have free hosting for your site.

Vosao is animated through a Google Group at :

https://groups.google.com/forum/#!forum/vosao-cms-development

A default site example is at http://monvosao.appspot.com and the cms is at http://monvosao.appspot.com/cms/ (ending slash is important!). 

Feel free to authenticate and test the rendering with those credentials : "admin@test.com" / "admin".

### Prerequisites

1 - You must have a Google App Engine application : see https://cloud.google.com/appengine/

2 - Download and install the latest GAE SDK for Java : see https://cloud.google.com/appengine/downloads and the JRE version prerequisite.

3 - Install a JRE (version 1.8 at least for GAE SDK 1.9.64)

### Installing Vosao

1 - Download the latest Vosao version (vosaocms-1.x.y.war) from Vosao Google Drive. See : https://drive.google.com/folderviewid=0B5kyNpiXIjiiV0lsa2ZRZHVFRjQ&usp=sharing

2 - Unpack vosaocms-1.x.y.war

3 - In the unpacked directory, find the file WEB-INF/appengine-web.xml and change the GAE application name to your application

4 - Upload the CMS application by issuing the GAE SDK command line: 

	appcfg.cmd --oauth2 update 'unpacked war directory'

### Using Vosao

1 - Go to your GAE application site which is at http://yourapplicationname.appspot.com

2 - The cms to create your site is at http://yourapplicationname.appspot.com/cms/ (with end /)

3 - Please authenticate with user / pwd : "admin@test.com" / "admin"

4 - For first use, go to __Configuration__ and click __Init__ or __Load default site__ button.

5 - When logging out, you are redirected to the default site home page.

### Building

To start development on Vosao CMS you will need to :

1 - Install Git & retrieve "vosaocms" project from this GitHub Repository.

2 - Install Maven.

3 - Launch __build.bat__. The war is created in "web/target" directory. 

### Running locally

1 - Launch __run.bat__. An empty datastore is created, if it doesnt already exist.

2 - Go first to http://localhost:8080 to init default user ( "admin@test.com" / "admin" )

3 - Go and log into cms at http://localhost:8080/cms/

4 - Go to __Configuration__ Panel and click to __Init__ or __Load default Site__ button.

5 - When logging out, you are redirected to the default site home page.
