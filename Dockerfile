##Baseimage /it has to be in any dockerfile
#FROM openjdk:11
##for writing maintainer of dockerfile
#LABEL maintainer="cydeo.com"
##to  direct any folder
#WORKDIR /usr/app
##will get jar file and copy to image
#COPY ./target/Spring-24-docker-0.0.1-SNAPSHOT.jar  ./app.jar
#
##will provide exacutable application when container starts the run
#ENTRYPOINT ["java","-jar", "app.jar"]

#FROM  openjdk:11
#COPY ./target/Spring-24-docker-0.0.1-SNAPSHOT.jar   /usr/app/
#WORKDIR /usr/app
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","Spring-21-Docker-0.0.1-SNAPSHOT.jar"]

##Baseimage /it has to be in any dockerfile
#FROM openjdk:11-jdk
##copy the whole jar file inside a user directory named app
#COPY ./target/Spring-24-docker-0.0.1-SNAPSHOT.jar  /usr/app/
##creating a directory for the image named app
#WORKDIR /usr/app
##creating an exacutable application when container starts the run
#ENTRYPOINT ["java","-jar","Spring-24-docker-0.0.1-SNAPSHOT.jar"]

#base image
FROM amd64/maven:3.8.6-openjdk-11
#storing the created image inside the user/app folder
WORKDIR user/app
#copying the project package and storing them in the above image directory
COPY . .
#if want to run mvn
ENTRYPOINT ["mvn", "spring-boot:run"]