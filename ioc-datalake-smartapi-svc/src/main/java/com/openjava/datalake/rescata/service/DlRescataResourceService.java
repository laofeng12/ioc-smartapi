package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlRescataResource;
import org.ljdp.component.exception.APIException;

/**
 * @Author JiaHai
 * @Description 资源目录宽表 业务层接口
 */
public interface DlRescataResourceService {

    /**
     * 根据主键resourceId查询单条记录（资源目录信息）
     *
     * @param resourceId 主键
     * @return
     */
    DlRescataResource findByResourceId(Long resourceId);

    /**
     * 内部对空值处理，根据ID获取Entity
     * 返回值保证非空，空就抛异常
     *
     * @param resourceId
     * @return
     * @throws APIException resourceId无效，查出来是空的就抛异常
     */
    DlRescataResource getByResourceId(Long resourceId) throws APIException;

    /**
     * 通过resourceId查最新的资源目录
     *
     * @param resourceId
     * @return
     * @throws APIException
     */
    DlRescataResource queryLatestByResourceId(Long resourceId) throws APIException;

    /**
     * 根据信息资源编码，查询最新版的资源目录
     *
     * @param resourceCode 信息资源编码
     * @return
     * @throws APIException
     */
    DlRescataResource queryLatestByResourceCode(String resourceCode) throws APIException;

}
