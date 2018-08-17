# steps to start from dev_docker image (depends on https://github.com/shanmuga-cv/dev_docker)

cd ~/git_repo/dev_docker/
# make build
make -f ~/git_repo/dev_docker/makefile scale_count=3

# rename containers
docker container rename dev_docker_dev_1 dev_kafka_server_1
docker container rename dev_docker_dev_2 dev_kafka_consumer_1
docker container rename dev_docker_dev_3 dev_kafka_producer_1

# start containers
docker container start dev_kafka_producer_1 dev_kafka_consumer_1 dev_kafka_server_1

# copy kafka binaries/jars into containers
tar -xf ~/Downloads/kafka_2.11-1.1.0.tar -C ~/Downloads
docker cp ~/Downloads/kafka_2.11-1.1.0 dev_kafka_producer_1:/opt
docker cp ~/Downloads/kafka_2.11-1.1.0 dev_kafka_consumer_1:/opt
docker cp ~/Downloads/kafka_2.11-1.1.0 dev_kafka_server_1:/opt
rm -r ~/Downloads/kafka_2.11-1.1.0

# start kafka server
docker exec dev_kafka_server_1 make -f /opt/git_repo/kafka_try/server/makefile start_server
sleep 2s # to allow kafka broker to come online

#create topic 
docker exec dev_kafka_server_1 make -f /opt/git_repo/kafka_try/server/makefile create_topic

# produce
# docker exec dev_kafka_producer_1 make -f /opt/git_repo/kafka_try/producer.makefile



# consumer
# docker exec dev_kafka_consumer_1 make -f /opt/git_repo/kafka_try/consumer.makefile

