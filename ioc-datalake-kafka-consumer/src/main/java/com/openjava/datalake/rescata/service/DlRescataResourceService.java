package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.vo.DlRescataResourceInfoVo;
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
     * 根据信息资源编码查询最新版资源目录
     *
     * @param resourceCode 信息资源编码
     * @return
     * @throws APIException
     */
    DlRescataResourceInfoVo findByResourceCode(String resourceCode) throws APIException;

    /**
     * 根据信息资源编码，查询最新版的资源目录
     *
     * @param resourceCode 信息资源编码
     * @return
     * @throws APIException
     */
    DlRescataResource queryLatestByResourceCode(String resourceCode) throws APIException;


    /**
     * 翻译单个资源目录的数据字典，并返回
     *
     * @param dlRescataResource
     * @return
     */
    DlRescataResourceInfoVo translateDlRescataResource(DlRescataResource dlRescataResource);
}
