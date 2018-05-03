kafka_home := ~/kafka_2.11-1.1.0

# Consumer
consoler_consumer: 
	cd $(kafka_home) && \
	bin/kafka-console-consumer.sh --bootstrap-server kafka_server_1:9092 --topic my_topic #--from-beginning
