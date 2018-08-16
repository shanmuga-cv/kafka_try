/** to copy offsets commited for an group_id to another group_id. */

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;

import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;

import java.util.stream.Collectors;

class CommitPartition {
    static String deploy_env = "live";
    static String bootstrap_server = "kafka-cloudera000.live.bi.hellofresh.io:9092";
    static String topic = "barcode_scanned_bridged";
    static String group_id = "dwh_consumber." + deploy_env + ".barcode_scanned";
    static String new_group_id = "dwh_consumer." + deploy_env + ".barcode_scanned";

    // static String bootstrap_server = "dev_kafka_server_1:9092";
    // static String topic = "spark_kafka_events";
    // static String group_id = "first_group";
    // static String new_group_id = "second";


    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrap_server);
        props.put("group.id", group_id);
        props.put("enable.auto.commit", "false");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        List<PartitionInfo> partitions = consumer.partitionsFor(topic);
        List<TopicPartition> topicPartitions = partitions.stream()
            .map(x -> new TopicPartition(x.topic(), x.partition()))
            .collect(Collectors.toList());
        // System.out.println(partitions);
        System.out.println("for "+group_id);
        System.out.println(topicPartitions);
        Map<TopicPartition,OffsetAndMetadata> offsets = new HashMap<TopicPartition,OffsetAndMetadata>();
        topicPartitions.forEach(x -> offsets.put(x, consumer.committed(x)));
        System.out.println(offsets);

        System.out.println("For "+new_group_id);
        props.put("group.id", new_group_id);
        KafkaConsumer<String, String> new_consumer = new KafkaConsumer<String, String>(props);
        Map<TopicPartition,OffsetAndMetadata> new_offsets = new HashMap<TopicPartition,OffsetAndMetadata>();
        topicPartitions.forEach(x -> new_offsets.put(x, new_consumer.committed(x)));
        // new_offsets.put(new TopicPartition("spark_kafka_events", 0), new OffsetAndMetadata(20L));
        System.out.println("before"+ new_offsets);
        System.out.println("committing to "+offsets);
        new_consumer.commitSync(offsets);
        System.out.println("After commiting");
        new_offsets.clear();
        topicPartitions.forEach(x -> new_offsets.put(x, new_consumer.committed(x)));
        System.out.println("after"+new_offsets);
    }
}