[1mdiff --git a/pom.xml b/pom.xml[m
[1mindex fe925fd..26670bf 100644[m
[1m--- a/pom.xml[m
[1m+++ b/pom.xml[m
[36m@@ -12,37 +12,37 @@[m
     <properties>[m
         <endorsed.dir>${project.build.directory}/endorsed</endorsed.dir>[m
         <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>[m
[31m-        [m
[32m+[m
         <!-- TODO: Add the URL to your own tomcat server. Used to deploy your WAR-file -->[m
         <!-- IMPORTANT-1: If your server uses https (via NGINX) use your DOMAIN NAME and https below: -->[m
         <!-- IMPORTANT-2: Observe we are NOT using /manager/html but /(tomcat)/manager/text for script uploads -->[m
[31m-	<remote.server>https://sem3nicki.dk/tomcat/manager/text</remote.server>[m
[32m+[m[32m        <remote.server>https://sem3nicki.dk/tomcat/manager/text</remote.server>[m
 [m
         <!-- TODO: Add your own database name -->[m
         <!-- IMPORTANT-1: The database should exist on your droplet -->[m
         <db.name>exam</db.name>[m
     </properties>[m
[31m-    [m
[32m+[m
     <dependencies>[m
[31m-        [m
[32m+[m
         <dependency>[m
             <groupId>org.eclipse.persistence</groupId>[m
             <artifactId>org.eclipse.persistence.jpa</artifactId>[m
             <version>2.7.7</version>[m
         </dependency>[m
[31m-        [m
[32m+[m
         <dependency>[m
             <groupId>mysql</groupId>[m
             <artifactId>mysql-connector-java</artifactId>[m
             <version>8.0.26</version>[m
         </dependency>[m
[31m-      [m
[32m+[m
         <dependency>[m
             <groupId>org.glassfish.jersey.bundles</groupId>[m
             <artifactId>jaxrs-ri</artifactId>[m
             <version>2.32</version>[m
         </dependency>[m
[31m-               [m
[32m+[m
         <dependency>[m
             <groupId>javax</groupId>[m
             <artifactId>javaee-web-api</artifactId>[m
[36m@@ -54,7 +54,7 @@[m
             <artifactId>gson</artifactId>[m
             <version>2.8.6</version>[m
         </dependency>[m
[31m-        [m
[32m+[m
         <!-- Test dependencies -->[m
         <dependency>[m
             <groupId>org.junit.jupiter</groupId>[m
[36m@@ -80,7 +80,7 @@[m
             <version>1.3</version>[m
             <scope>test</scope>[m
         </dependency>[m
[31m-        [m
[32m+[m
         <!-- Grizly Server used to test REST endpoints -->[m
         <dependency>[m
             <groupId>org.glassfish.jersey.containers</groupId>[m
[36m@@ -88,23 +88,23 @@[m
             <version>2.32</version>[m
             <scope>test</scope>[m
         </dependency>[m
[31m-        [m
[32m+[m
         <dependency>[m
             <groupId>io.rest-assured</groupId>[m
             <artifactId>rest-assured</artifactId>[m
             <version>4.3.1</version>[m
             <scope>test</scope>[m
         </dependency>[m
[31m-        [m
[32m+[m
         <!-- Security Dependencies (jwt-support) -->[m
[31m-        [m
[32m+[m
         <!-- https://mvnrepository.com/artifact/com.nimbusds/nimbus-jose-jwt -->[m
         <dependency>[m
             <groupId>com.nimbusds</groupId>[m
             <artifactId>nimbus-jose-jwt</artifactId>[m
             <version>9.0.1</version>[m
         </dependency>[m
[31m-        [m
[32m+[m
         <dependency>[m
             <groupId>org.mindrot</groupId>[m
             <artifactId>jbcrypt</artifactId>[m
[36m@@ -121,22 +121,22 @@[m
     </dependencies>[m
 [m
     <build>[m
[31m-       [m
[32m+[m
         <plugins>[m
             <!-- Plugin used to deploy to remote server -->[m
             <plugin>[m
                 <groupId>org.apache.tomcat.maven</groupId>[m
                 <artifactId>tomcat7-maven-plugin</artifactId>[m
                 <version>2.2</version>[m
[31m-                <configuration>          [m
[32m+[m[32m                <configuration>[m
                     <server>TomcatServer</server>[m
                     <url>${remote.server}</url>[m
                     <username>${remote.user}</username>[m
                     <password>${remote.password}</password>[m
[31m-                    <update>true</update>    [m
[31m-                </configuration>  [m
[31m-            </plugin>          [m
[31m-            [m
[32m+[m[32m                    <update>true</update>[m
[32m+[m[32m                </configuration>[m
[32m+[m[32m            </plugin>[m
[32m+[m
             <!--This is used to enable @BeforeAll etc used in jUnit 5 -->[m
             <plugin>[m
                 <groupId>org.apache.maven.plugins</groupId>[m
[36m@@ -155,7 +155,7 @@[m
                     </dependency>[m
                 </dependencies>[m
             </plugin>[m
[31m-           [m
[32m+[m
             <plugin>[m
                 <groupId>org.apache.maven.plugins</groupId>[m
                 <artifactId>maven-compiler-plugin</artifactId>[m
