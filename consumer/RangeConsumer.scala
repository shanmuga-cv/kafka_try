
import java.util.Properties

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.clients.consumer.ConsumerRecord

import scala.collection.JavaConverters._


class RangeConsumer[K, V](val kafkaProps: Properties, topics: scala.List[String], maxOffsets: Map[Int, Long]) extends Iterator[ConsumerRecord[K, V]] {
  private val consumer = new KafkaConsumer[K, V](kafkaProps)
  consumer.subscribe(topics.asJava)
  val maxRetries = 10
  private val pollDuration = 1000L
  var isFinished: Boolean = false

  var records: scala.Iterator[ConsumerRecord[K, V]] = scala.Iterator.empty

  private def poll(): Unit = {
    records = consumer.poll(pollDuration).iterator().asScala.filter(filter)
    consumer.commitAsync()
  }

  def filter(consumerRecord: ConsumerRecord[K, V]): Boolean = {
    consumerRecord.offset() <= maxOffsets(consumerRecord.partition())
  }

  override def hasNext(): Boolean = {
    var retryCount = maxRetries
    while (retryCount > 0) {
      if (records.hasNext)
        return true
      else {
        poll()
        retryCount -= 1
      }
    }
    false
  }

  override def next(): ConsumerRecord[K, V] = {
    if (hasNext()) {
      val record = records.next()
      return record
    } else {
      consumer.close()
      throw new Exception("didn't get any records")
    }
  }
}
