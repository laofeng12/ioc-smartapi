package com.openjava.datalake.push.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface SyncService {
   void insertDate(ConsumerRecord<String, String> record) throws Exception;
   void update(ConsumerRecord<String, String> record)throws Exception ;
   void deleteData(ConsumerRecord<String, String> record)throws Exception ;
}
