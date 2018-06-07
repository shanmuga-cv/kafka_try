package consumer;

import java.util.Properties;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;



public class SimpleConsumer {
    public static void main(String []args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "kafk_spark_server_1:9092");
        props.put("group.id", "first_group");
        props.put("client.id", "consumer_1");
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Arrays.asList("spark_kafka_events"));
        final int minBatchSize = 200;
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            System.out.println("done " + records.count());
            for (ConsumerRecord<String, String> record : records) {
                System.out.println(record);
            }
            consumer.commitSync();
        }
    }
}