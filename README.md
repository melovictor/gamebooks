gamebooks
=========

##configuration-dev.preferences

directory.savegame.dev=c:/gamebooks/save # pick one you like

directory.logs.dev=c:/gamebooks/logs # pick one you like

login.username=gnome # must be present in security-beans.xml


##tomcat launch configuration arguments:

-Dspring.profiles.active=dev

-Drebel.workspace.path=c:\gamebooks\source # point it to the local git repo

-Drebel.log=debug
