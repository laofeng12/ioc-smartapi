package com.openjava.datalake.push.repository;

import com.openjava.datalake.push.domain.DlApiPushLog;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 推送数据日志数据库访问层
 * @author xjd
 *
 */
public interface DlApiPushLogRepository extends DynamicJpaRepository<DlApiPushLog, String>, DlApiPushLogRepositoryCustom{
    /**
     * 根据pushSequence获取对账信息
     * @param pushSequence
     * @return
     */
    DlApiPushLog findByPushSequence(String pushSequence);
}
