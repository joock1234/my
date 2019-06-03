package com.szrhtf.task.service.test;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import java.util.Properties;

/**
 * kafka.javaapi.producer.Producer
 * */
public class KafkaProducerOld {

    private Producer<String, String> producer;

    public final static String TOPIC = "didi-topic-test";

    private KafkaProducerOld() {
        Properties props = new Properties();
        props.put("metadata.broker.list", "10.95.177.192:9092,10.95.97.175:9092");
        props.put("serializer.class", "kafka.serializer.StringEncoder");
        ProducerConfig config = new ProducerConfig(props);
        producer = new Producer<String, String>(config);
    }

    public void produce() {
        int messageNo = 0;
        final int COUNT = 10;

        while(messageNo < COUNT) {
            String data = String.format("hello kafka.javaapi.producer message %d", messageNo);

            KeyedMessage<String, String> msg = new KeyedMessage<String, String>(TOPIC, data);

            try {
                producer.send(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }

            messageNo++;
        }

        producer.close();
    }

    public static void main(String[] args) {
        new KafkaProducerOld().produce();
    }

}
