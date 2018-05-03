kafka_home := ~/kafka_2.11-1.1.0

start_server: start_zookeeper start_kafka
	echo started

stop_server: stop_kafka stop_zookeeper
	echo stoped

stop_kafka:
	cd $(kafka_home) && \
	bin/kafka-server-stop.sh

stop_zookeeper:
	cd $(kafka_home) && \
	bin/zookeeper-server-stop.sh

start_zookeeper:
	cd $(kafka_home) && \
	bin/zookeeper-server-start.sh -daemon config/zookeeper.properties

start_kafka:
	cd $(kafka_home) && \
	bin/kafka-server-start.sh -daemon config/server.properties

create_topic:
	cd $(kafka_home) && \
	bin/kafka-topics.sh --create --zookeeper kafka_server_1:2181 --replication-factor 1 --partitions 1 --topic my_topic

list_topic:
	cd $(kafka_home) && \
	bin/kafka-topics.sh --zookeeper kafka_server_1:2181 --list

describe_topic:
	cd $(kafka_home) && \
	bin/kafka-topics.sh --zookeeper kafka_server_1:2181 --describe --topic my_topic