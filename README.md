To show case kafka.

Built with `kafka_2.11-1.1.0.tar`. If different version of jar is used makefiles and Dockerfiles need to be changed. `kafka_2.11-1.1.0.tar` is expected in the directory. 

**setup**:
- requires docker to be installed.
- `make` command will create container for kafka_server, producer and consumer.

this directory will be mounted in the containers at `/opt/git_repo/kafka_try`.  
Refer to `consumer.makefile`, `producer.makefile` and `server/makefile`. These should be run inside the containers.