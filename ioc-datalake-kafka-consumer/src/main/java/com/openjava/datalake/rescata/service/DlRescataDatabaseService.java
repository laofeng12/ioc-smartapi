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
     * 根据分库类别、数据库类型，查询唯一数据库信息
     *
     * @param repositoryType 分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）
     * @param databaseType   数据库类型（1HIVE、2MPP）
     * @return
     * @throws APIException
     */
    DlRescataDatabase findByRepositoryTypeAndDatabaseType(Long repositoryType, Long databaseType) throws APIException;

    /**
     * 根据信息资源编码查询多个数据库连接信息
     *
     * @param resourceCode 信息资源编码
     * @return
     */
    List<DlRescataDatabase> findByResourceCode(String resourceCode);

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

    /**
     * 查询用于数据湖库表挂载使用的
     *
     * @param isUseForMount 是否用于数据挂载（1是、2否）
     * @return
     */
    List<DlRescataDatabase> findByIsUseForMount(Long isUseForMount);

    /**
     * 根据主键查询
     *
     * @param databaseId 数据库编码
     * @return
     */
    DlRescataDatabase findByDatabaseId(Long databaseId);
}
