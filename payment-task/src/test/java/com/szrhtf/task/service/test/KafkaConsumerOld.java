package com.szrhtf.task.service.test;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.serializer.StringDecoder;
import kafka.utils.VerifiableProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
* Consumer: kafka.javaapi.consumer.Consumer
* */
public class KafkaConsumerOld {

    private final ConsumerConnector consumerConnector;

    public final static String TOPIC = "didi-topic-test";

    private KafkaConsumerOld() {
        Properties props = new Properties();
        props.put("zookeeper.connect", "10.95.177.192:2181,10.95.97.175:2181,10.95.176.44:2181");
        props.put("group.id", "group-1");

        //zk连接超时
        props.put("zookeeper.session.timeout.ms", "400");
        props.put("zookeeper.sync.time.ms", "200");
        props.put("auto.commit.interval.ms", "1000");
        props.put("auto.offset.reset", "smallest");
        //序列化类
        props.put("serializer.class", "kafka.serializer.StringDecoder");

        ConsumerConfig config = new ConsumerConfig(props);

        consumerConnector = kafka.consumer.Consumer.createJavaConsumerConnector(config);
    }

    public void consume() {
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(KafkaProducerNew.TOPIC, new Integer(1));

        StringDecoder keyDecoder = new StringDecoder(new VerifiableProperties());
        StringDecoder valueDecoder = new StringDecoder(new VerifiableProperties());

        Map<String, List<KafkaStream<String, String>>> consumerMap = consumerConnector.createMessageStreams(topicCountMap, keyDecoder, valueDecoder);

        List<KafkaStream<String, String>> streams = consumerMap.get(TOPIC);
        for (final KafkaStream stream : streams) {
            ConsumerIterator<String, String> it = stream.iterator();
            while (it.hasNext()) {
                System.out.println("this is kafka consumer : " + new String( it.next().message().toString()) );
            }
        }
    }

    public static void main(String[] args) {
        new KafkaConsumerOld().consume();
    }
}
