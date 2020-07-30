package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlResourceDatasource;

/**
 * @Author JiaHai
 * @Description 资源目录关联数据源（汇聚） 业务层接口
 */
public interface DlResourceDatasourceService {

    /**
     * 根据信息资源编码查询
     *
     * @param resourceCode
     * @return
     */
    DlResourceDatasource findByResourceCode(String resourceCode);

}
