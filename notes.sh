# TODO understand transactiones
# TODO try out consumer gruops

# docker start existing continaer
docker container start -i -a 8f24a64b4dfe

# create docker container form image
docker run -t -i openshift/base-centos7 /bin/bash

# docker list all containers 
docker container ls -a

# docker remove continer
docker container rm 49fa46ad74e4

# copy files into docker
docker cp kafka_2.11-1.1.0.tar 8f24a64b4dfe:/opt/app-root/src/

# start kafka server
bin/zookeeper-server-start.sh -daemon config/zookeeper.properties
bin/kafka-server-start.sh -daemon config/server.properties

# create topic
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic my_topic

# trials
# 1 run kafka server, producer and cosumer
# 2 run kafka server with 3 partition topic, run consumer group with 1, 2, 3 consumers
# 3 run kafka cluster with 3 broker and 3 partiton topic and cosumer group

docker run -t -i -name kafka_server openshift/base-centos7 /bin/bash

# create kafka container form image
docker run -t -i --name kafka_broker_1 --network kafka_try_network kafka_try

docker run -t -i -v /Users/shanmuga.chidambaravel/git_repo/kafka_try:/opt/kafka_try --name kafka_producer_1 --network kafka_try_network kafka_try
docker run -t -i -v /Users/shanmuga.chidambaravel/git_repo/kafka_try:/opt/kafka_try --name kafka_consumer_1 --network kafka_try_network kafka_try
docker run -i -t -v /Users/shanmuga.chidambaravel/git_repo/kafka_try:/opt/kafka_try --name kafka_server_1  --network kafka_try_network kafka_try