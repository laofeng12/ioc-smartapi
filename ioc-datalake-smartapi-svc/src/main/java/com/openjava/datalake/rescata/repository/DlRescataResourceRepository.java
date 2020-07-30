package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlRescataResource;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录 数据库访问层
 */
public interface DlRescataResourceRepository extends DynamicJpaRepository<DlRescataResource, Long> {
    /**
     * 根据主键resourceId查询单条记录
     *
     * @param resourceId 主键
     * @return
     */
    DlRescataResource findByResourceId(Long resourceId);

    @Query(value = "select r1 from DlRescataResource r1 ,DlRescataResource r2 " +
            "where r1.resourceCode = r2.resourceCode " +
            "and r2.resourceId = :resourceId and r1.isLatest = :isLatest ")
    DlRescataResource findByResourceIdAndIsLatest(Long resourceId, Long isLatest);

    /**
     * 根据信息资源编码和“是否为最新版”查询对应的资源目录（主要用于查询最新版）
     *
     * @param resourceCode 信息资源编码
     * @param isLatest     最新版为1
     * @return
     */
    List<DlRescataResource> findByResourceCodeAndIsLatest(String resourceCode, Long isLatest);

}
