package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlResourceDatasource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录关联数据源（汇聚） 数据库访问层
 */
public interface DlResourceDatasourceRepository extends JpaRepository<DlResourceDatasource, Long> {
    /**
     * 根据信息资源编码删除
     *
     * @param resourceCode
     */
    @Modifying
    @Query("DELETE FROM DlResourceDatasource d where d.resourceCode = :resourceCode")
    void deleteByResourceCode(String resourceCode);

    /**
     * 根据信息资源编码查询
     *
     * @param resourceCode 信息资源编码
     * @return
     */
    List<DlResourceDatasource> findByResourceCode(String resourceCode);

    /**
     * 查询 指定局的 资源目录表名 （结构化、最新版、去除草稿、外部数据源）
     *
     * @return
     */
    @Query("SELECT new DlResourceDatasource(d.primaryKeyId, d.resourceCode, d.databaseType, d.datasourceId, d.tableSource)" +
            "  FROM DlResourceDatasource d, DlRescataResource r" +
            " WHERE r.resourceCode = d.resourceCode AND r.isLatest = 1 AND r.resourceType = 1 AND r.sourceMode = 1 AND r.isInternalDataSource = 0 AND r.resourceState <> 4 ")
    List<DlResourceDatasource> findByCreateDeptTopId();
}
