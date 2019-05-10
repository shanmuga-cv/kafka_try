// scala code to commit offsets to kafka

// libraryDependencies += "org.apache.kafka" % "kafka-clients" % "1.1.1"

// run kafka_server:9092 my_topic my_group 0:3,1:2,2:0
// run kafka_server:9092 my_topic my_group beginning
// run kafka_server:9092 my_topic my_group end

import java.util.Properties;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;

import scala.collection.JavaConverters._

object KafkaOffsetManagement {
  def main(args: Array[String]): Unit = {
    val Array(bootstrap_server, topic, group_id, offsetsRequired) = args
      val props: Properties = new Properties();
      props.put("bootstrap.servers", bootstrap_server);
      props.put("group.id", group_id);
      props.put("enable.auto.commit", "false");
      props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
      props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

      val consumer: KafkaConsumer[String, String]  = new KafkaConsumer[String, String](props);
      val partitions = consumer.partitionsFor(topic)
      val topicPartitions = partitions.asScala.map(x => new TopicPartition(x.topic, x.partition))

      val offsetsToCommit = offsetsRequired match {
        case "beginning" => consumer.beginningOffsets(topicPartitions.asJava)
        case "end" =>  consumer.endOffsets(topicPartitions.asJava)
        case x => {
          val offsets: java.util.Map[TopicPartition, java.lang.Long] = new java.util.HashMap[TopicPartition, java.lang.Long]()
          x.split(",")
            .map(_.split(":"))
            .foreach(y => {
              val (partition, offset) = (y(0).toInt, y(1).toLong)
              offsets.put(new TopicPartition(topic, partition), offset)
            })
          offsets
        }
      }
      // TODO handle non-existant commits
      // val currentOffset = topicPartitions.map(x => (x, new java.lang.Long(consumer.committed(x).offset))).toMap.asJava
      // log(currentOffset, offsetsToCommit)

      val toCommit = offsetsToCommit.asScala.map( x=> (x._1, new OffsetAndMetadata(x._2))).asJava
      println(toCommit)
      // ------ consumer.commitSync(toCommit)
  }

  def log(currentOffset: java.util.Map[TopicPartition,java.lang.Long], 
          offsetsToCommit: java.util.Map[TopicPartition,java.lang.Long]): Unit = {
    val offsetsChange = for {
      (topicPartition, currentOffset) <- currentOffset.asScala
      toOffset = offsetsToCommit.get(topicPartition)
    } yield (topicPartition -> (currentOffset, toOffset))

    offsetsChange.foreach( x => {
      val (topicPartition, (currentOffset, toOffset)) = x
      println(f"${topicPartition}: ${currentOffset}->${toOffset} (${toOffset - currentOffset})")
    })
  }
}
