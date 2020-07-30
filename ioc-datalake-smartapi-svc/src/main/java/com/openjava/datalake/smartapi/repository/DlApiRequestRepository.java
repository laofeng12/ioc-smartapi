package com.openjava.datalake.smartapi.repository;

import com.openjava.datalake.smartapi.domain.DlApiRequest;
import org.ljdp.core.spring.data.DynamicJpaRepository;

import java.util.List;

/**
 * DL_API_REQUEST数据库访问层
 * @author zjf
 *
 */
public interface DlApiRequestRepository extends DynamicJpaRepository<DlApiRequest, Long>, DlApiRequestRepositoryCustom{

    /**
     * 删除接口所有的请求参数
     * @param queryid 查询接口ID
     * @return 返回删除条数
     */
    int deleteByQueryId(Long queryid);

    /**
     * 获取接口所有的请求参数列表
     * @param queryid 查询接口ID
     * @return 请求参数集合
     */
    List<DlApiRequest> findByQueryId(Long queryid);
}
