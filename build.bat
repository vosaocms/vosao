rem To run unit tests execution during build 
rem -remove Dmaven.test.skip=true


call mvn -Dappname=vosao Dmaven.test.skip=true clean package
pause