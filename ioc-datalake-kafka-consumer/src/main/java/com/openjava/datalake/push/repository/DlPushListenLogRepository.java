package com.openjava.datalake.push.repository;

import com.openjava.datalake.push.domain.DlPushListenLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 监听事件日志记录数据库访问层
 * @author zmk
 *
 */
public interface DlPushListenLogRepository extends DynamicJpaRepository<DlPushListenLog, String>, DlPushListenLogRepositoryCustom{

//    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select t.* from DL_PUSH_LISTEN_LOG t,RETRY_RULE t2,RETRY_TIME t3 where t.RETRY_RULE_ID=t2.RETRY_RULE_ID and t2.RETRY_RULE_ID = t3.RETRY_RULE_ID and (t.RETRY_TIMES=t3.NUMBER_OF_TIMES) and t.STATES in (1,0) and t.RETRY_TIMES<=t2.RETRY_TIMES and TO_NUMBER(SYSDATE-t.UPDATE_TIME) * 24 * 60 * 60 * 1000 >=t3.TIMES for update ",nativeQuery=true)
    List<DlPushListenLog> getNobegin();
}
