Java-Game-Server-Advanced-Java-Course-Mail.ru
=============================================

Game server for net game "click more" in browser.

Build: For properly work need to include all libs in lib/Required folder or use Maven to get all dependencies.

For a DB service work you can use HibernateDatabaseService, JdbcDatabaseService or mock AccountService. See line 46 in server.main.ServerStarter class.
For HibernateDatabaseService or JdbcDatabaseService: MySql service must be started, Db and table must be created. See settings in src.server.main.ServerSettings and data folder.

To play: 
1. You can build project from sources. Start server using ServerStarter class, run browser except IE, and input by default localhost:8080. Folders data, static and templates with all files must be in the root directory of the project.
2. You can launch jar file GameServer-1.0-jar-with-dependencies.jar from the folder "target" using command line: java -jar GameServer-1.0-jar-with-dependencies.jar. Folders data, static and templates with all files must be in the same directory as GameServer-1.0-jar-with-dependencies.jar

