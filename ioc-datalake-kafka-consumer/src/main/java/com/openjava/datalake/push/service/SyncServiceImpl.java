package com.openjava.datalake.push.service;

import com.alibaba.fastjson.JSONObject;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.dataxjdbcutil.util.DataLakeWriter;
import com.openjava.datalake.push.dao.DlPushListenLogMapper;
import com.openjava.datalake.push.domain.DlApiPushAudit;
import com.openjava.datalake.push.domain.DlPushListenLog;
import com.openjava.datalake.push.service.ApiSyncService;
import com.openjava.datalake.push.service.DlApiPushAuditService;
import com.openjava.datalake.push.service.DlApiPushListenService;
import com.openjava.datalake.push.service.DlApiPushLogService;
import com.openjava.datalake.push.vo.KafkaApiSyncMessageVO;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Connection;
import java.util.Date;

@Service
public class SyncServiceImpl implements SyncService {
    private static final Logger LOG = LoggerFactory.getLogger(SyncServiceImpl.class);
    @Resource
    private ApiSyncService apiSyncService;
    @Resource
    private DlApiPushLogService dlApiPushLogService;
    @Resource
    private DlApiPushListenService dlApiPushListenService;
    @Resource
    private DlPushListenLogMapper dlPushListenLogMapper;
    @Resource
    private DlApiPushAuditService dlApiPushAuditService;

    public void insertDate(ConsumerRecord<String, String> record) throws Exception {
        KafkaApiSyncMessageVO messageVO = JSONObject.parseObject(record.value(),KafkaApiSyncMessageVO.class);
        System.out.println("insert");
        System.out.println(record.value());
        //保存审计信息
        Long resourceId = Long.valueOf(record.key()+"");
        DlApiPushAudit dapa = JSONObject.parseObject(messageVO.getSyncReceiveDataVO().getAudit_info(),DlApiPushAudit.class);
        dapa.setApiCode(resourceId+"");
        dlApiPushAuditService.doSave(dapa);
        //step2:同步数据
        Connection connection = apiSyncService.getConnThroughHikarByResId(resourceId);
        DataLakeWriter dataLakeWriter = new DataLakeWriter(messageVO.getTableContext());
        dataLakeWriter.doBatchInsert(connection, messageVO.getTableContext());
        org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);//关闭事务  todo 失败时没关闭连接
        //更新日志
        System.out.println(messageVO.getRecordSequence());
        dlApiPushLogService.updateApiPushLog(messageVO.getRecordSequence(),200L,"同步成功");
        //step2：保存监听
        Date now = new Date();
        DlPushListenLog listenLog = new DlPushListenLog();
        listenLog.setRecordSequences(messageVO.getRecordSequence());//推送事件唯一序列号
        listenLog.setBid(messageVO.getResourceId()+"");//业务员id
        listenLog.setRetryTimes(0L);//当前重试次数
        listenLog.setCreateTime(now);
        listenLog.setStates(PublicConstant.DL_EVENT_PROCESS_LOG_NO_BEGIN);
        listenLog.setJsonBody(record.value());
        listenLog.setUpdateTime(now);
        listenLog.setRetryRuleId(PublicConstant.DL_RETRY_RULD_ID);
        dlPushListenLogMapper.insert(listenLog);
    }

    public void update(ConsumerRecord<String, String> record)throws Exception {
        KafkaApiSyncMessageVO messageVO = JSONObject.parseObject(record.value(),KafkaApiSyncMessageVO.class);
        System.out.println("update");
        System.out.println(record.value());
        //保存审计信息
        Long resourceId = Long.valueOf(record.key()+"");
        DlApiPushAudit dapa = JSONObject.parseObject(messageVO.getSyncReceiveDataVO().getAudit_info(),DlApiPushAudit.class);
        dapa.setApiCode(resourceId+"");
        dlApiPushAuditService.doSave(dapa);
        //step2:更新数据
        Connection connection = apiSyncService.getConnThroughHikarByResId(resourceId);
        DataLakeWriter dataLakeWriter = new DataLakeWriter(messageVO.getTableContext());
        dataLakeWriter.doUpdateBatchExecute(connection, messageVO.getTableContext());
        org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);//关闭事务  todo 失败时没关闭连接
        //更新日志
        dlApiPushLogService.updateApiPushLog(messageVO.getRecordSequence(),200L,"同步成功");
        //step2：保存监听
        Date now = new Date();
        DlPushListenLog listenLog = new DlPushListenLog();
        listenLog.setRecordSequences(messageVO.getRecordSequence());//推送事件唯一序列号
        listenLog.setBid(messageVO.getResourceId()+"");//业务员id
        listenLog.setRetryTimes(0L);//当前重试次数
        listenLog.setCreateTime(now);
        listenLog.setStates(PublicConstant.DL_EVENT_PROCESS_LOG_NO_BEGIN);
        listenLog.setJsonBody(record.value());
        listenLog.setUpdateTime(now);
        listenLog.setRetryRuleId(PublicConstant.DL_RETRY_RULD_ID);
        dlPushListenLogMapper.insert(listenLog);
    }

    public void deleteData(ConsumerRecord<String, String> record)throws Exception {
        KafkaApiSyncMessageVO messageVO = JSONObject.parseObject(record.value(),KafkaApiSyncMessageVO.class);
        System.out.println("delete");
        System.out.println(record.value());
        //保存审计信息
        Long resourceId = Long.valueOf(record.key()+"");
        DlApiPushAudit dapa = JSONObject.parseObject(messageVO.getSyncReceiveDataVO().getAudit_info(),DlApiPushAudit.class);
        dapa.setApiCode(resourceId+"");
        dlApiPushAuditService.doSave(dapa);
        //step2:删除数据
        Connection connection = apiSyncService.getConnThroughHikarByResId(resourceId);
        DataLakeWriter dataLakeWriter = new DataLakeWriter(messageVO.getTableContext());
        dataLakeWriter.setDelete(true);
        dataLakeWriter.doDeleteBatchExecute(connection, messageVO.getTableContext());
        org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);//关闭事务  todo 失败时没关闭连接
        //更新日志
        dlApiPushLogService.updateApiPushLog(messageVO.getRecordSequence(),200L,"同步成功");
        //step2：保存监听
        Date now = new Date();
        DlPushListenLog listenLog = new DlPushListenLog();
        listenLog.setRecordSequences(messageVO.getRecordSequence());//推送事件唯一序列号
        listenLog.setBid(messageVO.getResourceId()+"");//业务员id
        listenLog.setRetryTimes(0L);//当前重试次数
        listenLog.setCreateTime(now);
        listenLog.setStates(PublicConstant.DL_EVENT_PROCESS_LOG_NO_BEGIN);
        listenLog.setJsonBody(record.value());
        listenLog.setUpdateTime(now);
        listenLog.setRetryRuleId(PublicConstant.DL_RETRY_RULD_ID);
        dlPushListenLogMapper.insert(listenLog);
    }

}
