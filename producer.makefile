kafka_home := ~/kafka_2.11-1.1.0

run_console_producer:
	cd $(kafka_home) && \
	bin/kafka-console-producer.sh --broker-list kafka_server_1:9092 --topic my_topic
