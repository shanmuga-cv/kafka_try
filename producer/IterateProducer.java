package producer;

import java.util.Properties;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


public class IterateProducer {
    public static void main(String args[]) throws InterruptedException {
        int sleep_time = Integer.parseInt(args[0]);
        int no_messages = Integer.parseInt(args[1]);
        String msg_prefix = args[2];

        Properties props = new Properties();
        props.put("bootstrap.servers", "kafka_server_1:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer<>(props);
        for(int i = 0; i < no_messages; i++) {
            Thread.sleep(sleep_time);
            String message = String.format("%s %d", msg_prefix, i);
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("my_topic", Integer.toString(i), message);
            System.out.println("sending msg " + record);
            producer.send(record);
        }

        producer.close();
    }
}