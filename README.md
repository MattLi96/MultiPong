# Multi-Pong
An application for playing multiplayer pong. Supports and arbitrary number of players, though it works best from 2 to 4 players.

# Dependencies
This project runs on Java 8 and requires the JavaFX libraries. The JavaFX library should be included in Java 8.

Additionally this project requires gson for the networking features. A gson jar has been provided in the jars folder.

Lastly, the servlet for this project runs on an Apache Tomcat 7 server. This means you will need to setup Apache Tomcat 7 server and add all the appropriate libraries. You will need to add the dynamic web module facet to your project.

If you are using eclipse, in the end your build path should have: gson.jar, Apache Tomcat v7.0 library, EAR libraries, JRE System Library (Java 8), and Web App Libraries.

# Main Classes
The main class for the client is  src/client/gui/GUI.java
The main class for the servlet is src/servlet/Servlet.java

An executable jar of the client has been provided in the jars folder. The required libraries for it are packaged in.

I have not created a runnable jar for the Servlet, you will need to setup the project and run it manually.

# Acknowledgment
This project is based on code written at Winchester High School by the APCS class of 2010 and OCCS class of 2014.