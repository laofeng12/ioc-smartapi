package com.openjava.datalake.smartapi.repository;

import com.openjava.datalake.smartapi.domain.DlApiResponse;
import org.ljdp.core.spring.data.DynamicJpaRepository;

import java.util.List;

/**
 * DL_API_RESPONSE数据库访问层
 * @author zjf
 *
 */
public interface DlApiResponseRepository extends DynamicJpaRepository<DlApiResponse, Long>, DlApiResponseRepositoryCustom {
    /**
     * 删除接口所有的返回参数
     * @param queryid 查询接口ID
     * @return 返回删除条数
     */
    int deleteByQueryId(Long queryid);

    /**
     * 获取接口所有的返回参数列表
     * @param queryid 查询接口ID
     * @return 请求参数集合
     */
    List<DlApiResponse> findByQueryId(Long queryid);
}
