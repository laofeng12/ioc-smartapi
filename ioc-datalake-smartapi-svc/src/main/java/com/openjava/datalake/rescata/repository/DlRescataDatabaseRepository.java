package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlRescataDatabase;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 数据库连接参数信息 数据库访问层
 */
public interface DlRescataDatabaseRepository extends DynamicJpaRepository<DlRescataDatabase, Long> {
    /**
     * 根据分库类别、数据库类型，查询唯一数据库信息
     *
     * @param repositoryType 分库类别（dl.resource.repository.type）（1归集库、2中心库、3基础库、4主题库、5专题库）
     * @param databaseType   数据库类型（1HIVE、2MPP）
     * @return
     */
    DlRescataDatabase findByRepositoryTypeAndDatabaseType(Long repositoryType, Long databaseType);

//    /**
//     * 根据resourceCode查询多个数据库连接信息
//     *
//     * @param resourceCode 信息资源编码
//     * @return
//     */
//    @Query("SELECT new DlRescataDatabase(d.databaseId, d.databaseName, d.databaseJsonInfo, d.repositoryType, d.databaseType, d.isUseForMount) FROM DlRescataDatabase d, DlRescataResourceDatabase r WHERE d.databaseId = r.databaseId AND r.resourceCode = :resourceCode")
//    List<DlRescataDatabase> findByResourceCode(String resourceCode);

    /**
     * 根据信息资源编码和数据库类型查询
     *
     * @param resourceCode 信息资源编码
     * @param databaseType 数据库类型（1HIVE、2MPP）
     * @return
     */
    @Query("SELECT new DlRescataDatabase(d.databaseId, d.databaseName, d.databaseJsonInfo, d.repositoryType, d.databaseType, d.isUseForMount) FROM DlRescataDatabase d, DlRescataResourceDatabase r WHERE d.databaseId = r.databaseId AND r.resourceCode = :resourceCode AND d.databaseType = :databaseType")
    DlRescataDatabase findByResourceCodeAndRepositoryType(String resourceCode, Long databaseType);

    /**
     * 查询用于数据湖库表挂载使用的
     *
     * @param isUseForMount 是否用于数据挂载（1是、2否）
     * @return
     */
    @Query("FROM DlRescataDatabase d WHERE (d.isUseForMount = :isUseForMount OR :isUseForMount IS NULL)")
    List<DlRescataDatabase> findByIsUseForMount(Long isUseForMount);

    /**
     * 获取数据仓库类型
     * @param isDatastore  是否在数据仓库显示  1显示  0不显示
     * @return
     */
    @Query("from  DlRescataDatabase d where d.isDatastore= :isDatastore order by d.repositoryType asc ")
    List<DlRescataDatabase> findByIsDatastore(Long isDatastore);
}
