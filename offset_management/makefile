kafka_home := /opt/kafka_2.11-1.1.0

run: compile
	java -cp '.:$(kafka_home)/libs/*' CommitPartition

compile: clean
	javac -cp '.:$(kafka_home)/libs/*' CommitPartition.java

clean:
	rm -rf *.class