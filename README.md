gamebooks
=========

##configuration-dev.preferences

directory.savegame.dev=c:/gamebooks/save # pick one you like

directory.logs.dev=c:/gamebooks/logs # pick one you like

login.username=gnome # must be present in security-beans.xml


##tomcat launch configuration arguments:

-Dspring.profiles.active=dev

-Drebel.workspace.path=c:\gamebooks\source # for jrebel, point it to the local git repo

-Drebel.log=debug # for jrebel, and only if you want to hide a lot of useless console logs

-XX:MaxPermSize=256M # if you run into memory issues, this should be good enough for the moment
