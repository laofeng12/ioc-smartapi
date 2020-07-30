package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlRescataDatabase;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import org.ljdp.component.exception.APIException;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 数据库连接参数信息 业务层接口
 */
public interface DlRescataDatabaseService {

    /**
     * 根据信息资源编码和数据库类型查询数据库连接信息JSON
     *
     * @param resourceCode 信息资源编码
     * @param databaseType 数据库类型（1HIVE、2MPP）
     * @return
     * @throws APIException
     */
    DatabaseInfoVo findByResourceCodeAndRepositoryType(String resourceCode, Long databaseType) throws APIException;

    /**
     * 从数据库连接参数实体中获取数据库连接信息对象
     *
     * @param dlRescataDatabase
     * @return
     * @throws APIException
     */
    DatabaseInfoVo getDatabaseInfoVoFromDlRescataDatabase(DlRescataDatabase dlRescataDatabase) throws APIException;
}
