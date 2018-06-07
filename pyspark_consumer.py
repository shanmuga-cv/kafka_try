# spark-submit --packages org.apache.spark:spark-streaming-kafka_2.10:1.6.0 pyspark_consumer.py



from pyspark import SparkContext

from pyspark.streaming import StreamingContext
from pyspark.streaming.kafka import KafkaUtils

from pyspark.sql import types

sc = SparkContext(appName="streaming - checkpoint")
ssc = StreamingContext(sc, 2)
brokers = "kafk_spark_server_1:9092"
topic = "spark_kafka_events"
kvs = KafkaUtils.createDirectStream(ssc, [topic],{"metadata.broker.list": brokers, "auto.offset.reset": "smallest"})

kvs.pprint()
ssc.start()
ssc.awaitTermination()
