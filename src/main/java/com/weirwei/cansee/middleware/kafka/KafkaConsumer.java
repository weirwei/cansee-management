package com.weirwei.cansee.middleware.kafka;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author weirwei 2021/3/15 17:11
 */
@Component
@Slf4j
public class KafkaConsumer {

    private final String kafkaServer = "http://localhost:8080/kafka/consumer";

    @KafkaListener(topics = {"${cansee.log.df-kafkaconsumer.topic}"})
    public void onMessage(ConsumerRecord<?, ?> record, Acknowledgment ack) {
        log.info(record.toString());
        String recordJsonStr = record.value().toString();
        Map<String, Object> paramMap = new HashMap<>(1);
        paramMap.put("record", recordJsonStr);
        // 发送http 请求
        String result = HttpRequest.post(kafkaServer)
                .form(paramMap)
                .timeout(20000)
                .execute().body();
        JSONObject jsonObject = JSON.parseObject(result);
        String status = jsonObject.getString("status");
        // 返回值校验
        if (StringUtils.isEmpty(status) || StringUtils.equals(status, "fail")) {
            log.warn("consume log err, res:" + result);
        } else {
            ack.acknowledge();
        }
    }
}
