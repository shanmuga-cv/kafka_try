import java.util.Properties;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;


class CopyKafkaTopic {
    static String consumerGroupID = "dev";

    // TODO currently key.deserializer and value.deserializer is harcoded to be StringDeserializer
    static <K, V> KafkaConsumer<K, V> createKafkaConsumer(String master, List<String> topics) {
        Properties props = new Properties();
        props.put("bootstrap.servers", master);
        props.put("group.id", consumerGroupID);
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("auto.offset.reset", "earliest");
        props.put("max.poll.records", "10000");

        KafkaConsumer<K, V> consumer = new KafkaConsumer<K, V>(props);
        consumer.subscribe(topics);
        return consumer;
    }

    // TODO currently key.deserializer and value.deserializer is harcoded to be StringDeserializer
    static <K, V> KafkaProducer<K, V> createKafkaProducer(String master) {
        Properties props = new Properties();
        props.put("bootstrap.servers", master);
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384000);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<K, V> producer = new KafkaProducer<K, V>(props);
        return producer;

    }

    public static void main(String[] args) {
        if(args.length < 4) {
            System.out.println("Usage: srcKafkaMaster destKafkaMaster topic no_recocords");
            System.exit(1);
        }

        String srcMaster = args[0], destMaster = args[1], topic = args[2];
        Long noRecords = Long.parseLong(args[3]);

        KafkaConsumer<String, String> srcConsumer = CopyKafkaTopic.<String, String>createKafkaConsumer(srcMaster, Arrays.asList(topic));
        KafkaProducer<String, String> destProducer = createKafkaProducer(destMaster);
        while(noRecords >0) {
            ConsumerRecords<String, String> records = srcConsumer.poll(500);
            for(ConsumerRecord<String, String> r:records) {
                destProducer.send(
                        new ProducerRecord<String, String>(r.topic(),
                                null,
                                r.timestamp() >= 0 ? r.timestamp() : null,
                                r.key(),
                                r.value())
                );
            }
            noRecords -= records.count();
            System.err.println("Produced " + records.count() + "records, remaining "+ noRecords);
        }

        destProducer.close();
        srcConsumer.close();

    }
}
