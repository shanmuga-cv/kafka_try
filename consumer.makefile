kafka_home := /opt/app-root/src/kafka_2.11-1.1.0

# Java consumer
run_consumer: compile_consumer
	java -cp "bin:$(kafka_home)/libs/*" consumer.SimpleConsumer

compile_consumer: clean_consumer
	javac consumer/*.java -cp "$(kafka_home)/libs/*" -d bin

clean_consumer:
	rm -rf bin/consumer

# Console consumer
consoler_consumer: 
	cd $(kafka_home) && \
	bin/kafka-console-consumer.sh --bootstrap-server kafka_server_1:9092 --topic my_topic #--from-beginning
