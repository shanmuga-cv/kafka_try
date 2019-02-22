kafka_home := /opt/kafka_2.11-1.1.0
srcKafka ?= src_host:9092
destKafka ?= kafka_try_server_1:9092
kafkaTopic ?= my_topic

# Java producer
run: compile
	java -cp "bin:$(kafka_home)/libs/*" CopyKafkaTopic $(srcKafka) $(destKafka) $(kafkaTopic) 1000

compile: clean
	javac kafka_copy/*java -cp "$(kafka_home)/libs/*" -d bin

clean:
	rm -rf bin/*
