FROM tomcat:9.0.121-jdk17-temurin

COPY target/awsPra-1.0-SNAPSHOT.war \
     /usr/local/tomcat/webapps/awsPra-1.0-SNAPSHOT.war


RUN apt-get update && \
    apt-get install -y python3 python3-boto3 && \
    rm -rf /var/lib/apt/lists/*
