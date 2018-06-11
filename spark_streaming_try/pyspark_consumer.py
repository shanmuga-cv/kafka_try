#! spark-submit --packages org.apache.spark:spark-streaming-kafka_2.10:1.6.1 pyspark_consumer.py


from pyspark import SparkContext

from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

from pyspark.sql import types, SQLContext
import json


checkpoint_dir = "/spark_streaming_try/checkpoint"
def parse_message(r):
    offset, msg = r
    msg_header, msg_no = msg.split(" ")
    return (offset, msg, msg_header, msg_no)

def createStreamingContext():
    global checkpoint_dir
    sc = SparkContext(appName="Streaming")
    ssc = StreamingContext(sc, 2)

    brokers = "kafk_spark_server_1:9092"
    topic = "spark_kafka_events"
    kvs = KafkaUtils.createDirectStream(ssc, [topic],{
        "metadata.broker.list": brokers, 
        "auto.offset.reset": "smallest",
        "enable.auto.commit": "false",
        "group.id": "spark_consumer_group_1",
        "client.id": "spark_consumer_client_1"
    })

    kvs.map(parse_message).map(lambda x: json.dumps(x)).saveAsTextFiles("/spark_streaming_try/db_table/part", "txt")
    ssc.checkpoint(checkpoint_dir)
    return ssc

ssc = StreamingContext.getOrCreate(checkpoint_dir, createStreamingContext)
ssc.start()
ssc.awaitTermination()
