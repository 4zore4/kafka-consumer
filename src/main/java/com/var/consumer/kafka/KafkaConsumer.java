package com.var.consumer.kafka;

import com.alibaba.fastjson.JSON;
import com.var.consumer.components.InfluxComponent;
import com.var.consumer.domain.Stress;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Slf4j
public class KafkaConsumer {

    public static final String TOPIC_DEMO = "demo";

    public static final String TOPIC_GROUP = "topic.group1";

    @Autowired
    private InfluxComponent influxComponent;

    @KafkaListener(topics = TOPIC_DEMO,groupId = TOPIC_GROUP)
    public void topic_demo(List<ConsumerRecord> record, Acknowledgment ack){
        Iterator<ConsumerRecord> iterator = record.iterator();
//        Optional message = Optional.ofNullable(record.value());
        List<Stress> stressList = new ArrayList<>();
        while (iterator.hasNext()){
            Stress stress = JSON.parseObject(iterator.next().value().toString(),Stress.class);
            stressList.add(stress);
        }
        influxComponent.batchInsert(stressList);
        log.info("topic_demo 消费了,message:{}", stressList.size());
        ack.acknowledge();
    }
}