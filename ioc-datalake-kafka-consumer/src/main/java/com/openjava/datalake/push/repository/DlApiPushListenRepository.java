package com.openjava.datalake.push.repository;

import com.openjava.datalake.push.domain.DlApiPushListen;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 接口推送监听表数据库访问层
 * @author zmk
 *
 */
public interface DlApiPushListenRepository extends DynamicJpaRepository<DlApiPushListen, Long>, DlApiPushListenRepositoryCustom{

    DlApiPushListen findByResourceCodeAndCreateUser(String resourceCode, Long createUser);
}

