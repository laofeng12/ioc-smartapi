package com.openjava.datalake.push.repository;

import com.openjava.datalake.push.domain.RetryTime;
import org.ljdp.core.spring.data.DynamicJpaRepository;

/**
 * 重试时间表数据库访问层
 * @author zmk
 *
 */
public interface RetryTimeRepository extends DynamicJpaRepository<RetryTime, Long>, RetryTimeRepositoryCustom{
	
}
