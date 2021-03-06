kafka_home := /opt/kafka_2.11-1.1.0

# Java producer
run_producer: complie_producer
	java -cp "bin:$(kafka_home)/libs/*" producer.IterateProducer 500 5 test_message

complie_producer: clean_producer
	javac producer/*java -cp "$(kafka_home)/libs/*" -d bin

clean_producer:
	rm -rf bin/producer

# Console producer
run_console_producer:
	cd $(kafka_home) && \
	bin/kafka-console-producer.sh --broker-list kafka_server_1:9092 --topic my_topic
