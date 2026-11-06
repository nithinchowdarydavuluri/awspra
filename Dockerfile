FROM tomcat:9.0.121-jdk17-temurin

COPY awsPra/target/awsPra-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/awsPra-1.0-SNAPSHOT.war