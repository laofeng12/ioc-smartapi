package com.openjava.datalake.consumer;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datalake.push.service.DlApiPushLogService;
import com.openjava.datalake.push.service.SyncService;
import com.openjava.datalake.push.vo.KafkaApiSyncMessageVO;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import scala.util.Try;

import javax.annotation.Resource;
import java.util.List;

@Component
public class ApiSyncCounsumerComponent {
    private static final Logger LOG = LoggerFactory.getLogger(ApiSyncCounsumerComponent.class);
    @Resource
    private SyncService syncService;
    @Resource
    private DlApiPushLogService dlApiPushLogService;
    @KafkaListener(topics = {"datalake-topic1"},containerFactory = "batchContainerFactory")
    public void insertData(List<ConsumerRecord<String, String>> records, Acknowledgment ack, Consumer<String, String> consumer) throws InterruptedException {
        try{
            for (ConsumerRecord<String, String> record:records) {
                KafkaApiSyncMessageVO messageVO = JSONObject.parseObject(record.value(),KafkaApiSyncMessageVO.class);
                try {
                    syncService.insertDate(record);
                }catch (Exception e){
                    e.printStackTrace();
                    dlApiPushLogService.updateApiPushLog(messageVO.getRecordSequence(),-200L,e.getMessage());
                }
            }
            ack.acknowledge();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @KafkaListener(topics = {"datalake-topic2"},containerFactory = "batchContainerFactory")
    public void updateData(List<ConsumerRecord<String, String>> records, Acknowledgment ack, Consumer<String, String> consumer) throws InterruptedException {
        try{
            for (ConsumerRecord<String, String> record:records) {
                syncService.update(record);
            }
            ack.acknowledge();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @KafkaListener(topics = {"datalake-topic3"},containerFactory = "batchContainerFactory")
    public void deleteData(List<ConsumerRecord<String, String>> records, Acknowledgment ack, Consumer<String, String> consumer) throws InterruptedException {
        try{
            for (ConsumerRecord<String, String> record:records) {
                syncService.deleteData(record);
            }
            ack.acknowledge();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
