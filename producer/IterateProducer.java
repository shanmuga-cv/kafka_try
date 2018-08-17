package producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class IterateProducer {
    public static void main(String args[]) throws InterruptedException {
        int sleep_time = Integer.parseInt(args[0]);
        int no_messages = Integer.parseInt(args[1]);
        String message_prefix = args[2];
        String topic = "my_topic";

        Properties props = new Properties();
        props.put("bootstrap.servers", "dev_kafka_server_1:9092");
        props.put("acks", "all");
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        int no_partitions = producer.partitionsFor(topic).size();
        for(int i = 0; i < no_messages; i++) {
            Thread.sleep(sleep_time);
            ProducerRecord<String, String> record = makeRecord(topic, i, message_prefix, no_partitions);
            System.out.println("sending msg " + record);
            producer.send(record);
        }

        producer.close();
    }

    static ProducerRecord<String, String> makeRecord(String topic, int no, String message_prefix, int no_partitions) {
        int partition = no % no_partitions;
        String key = Integer.toString(no);
        String value = String.format("%s %d", message_prefix, no);
        return new ProducerRecord<String, String>(topic, partition, key, value);
    }
}