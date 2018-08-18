FROM ubuntu

RUN apt-get update && apt-get install -y make && apt-get install -y openjdk-8-jdk
ADD kafka_2.11-1.1.0.tar /opt/
