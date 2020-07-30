package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Set;

/**
 * @Author JiaHai
 * @Description 信息项 数据库访问层
 */
public interface DlRescataColumnRepository extends DynamicJpaRepository<DlRescataColumn, Long> {

    /**
     * 根据资源目录ID删除（多条）记录
     *
     * @param resourceId 资源目录ID
     */
    @Modifying
    @Query("DELETE FROM DlRescataColumn c WHERE c.resourceId = :resourceId")
    void deleteByResourceId(Long resourceId);

    /**
     * 根据一组资源目录ID删除（多条）记录
     *
     * @param resourceIdList
     */
    @Modifying
    @Query("DELETE FROM DlRescataColumn c WHERE c.resourceId IN :resourceIdList")
    void deleteByResourceIdList(List<Long> resourceIdList);

    /**
     * 根据资源目录ID，更新公开范围
     *
     * @param resourceId      资源目录ID
     * @param columnOpenScope 信息项公开范围
     */
    @Modifying
    @Query("UPDATE DlRescataColumn c SET c.columnOpenScope = :columnOpenScope WHERE c.resourceId = :resourceId")
    void updateColumnOpenScopeByResourceId(Long resourceId, Long columnOpenScope);

    /**
     * 根据主键structureId查询单条记录
     *
     * @param structureId 主键ID
     * @return
     */
    DlRescataColumn findByStructureId(Long structureId);

    /**
     * 根据资源目录resourceId查询并根据structureId顺序
     *
     * @param resourceId 资源目录ID
     * @return
     */
    List<DlRescataColumn> findByResourceIdAndIsDeleteOrderByStructureId(Long resourceId, Long isDelete);

    /**
     * 根据资源目录resourceId查询isList为指定值的、并根据structureId顺序
     *
     * @param resourceId 资源目录ID
     * @param isList     是否用于展示（1是、0否）
     * @return
     */
    List<DlRescataColumn> findByResourceIdAndIsListOrderByStructureId(Long resourceId, Long isList);

    /**
     * 根据资源目录resourceId查询isQuery为指定值的、并根据structureId顺序
     *
     * @param resourceId 资源目录ID
     * @param isQuery    是否用于查询（1是、0否）
     * @return
     */
    List<DlRescataColumn> findByResourceIdAndIsQueryOrderByStructureId(Long resourceId, Long isQuery);


    /**
     * 根据资源目录resourceId和字段名称查询
     *
     * @param pageable
     * @param resourceId 资源目录ID
     * @param columnName 字段名称
     * @return
     */
    @Query("FROM DlRescataColumn c" +
            " WHERE c.resourceId = :resourceId" +
            "  AND (c.columnName LIKE '%' || :columnName || '%' OR :columnName IS NULL)")
    Page<DlRescataColumn> findByResourceIdAndColumnName(Pageable pageable, Long resourceId, String columnName);

    /**
     * 根据一组信息项ID，查询
     *
     * @param structureIdList
     * @return
     */
    List<DlRescataColumn> findByStructureIdIn(List<Long> structureIdList);

    @Query(value = "from DlRescataColumn where structureId in (:structureIds) " +
            "and (resourceId = :resourceId or :resourceId is null)")
    List<DlRescataColumn> findByStructureIdInAndResourceId(Set<Long> structureIds, Long resourceId);

    /**
     * 根据资源ID查询
     * @param resourceId
     * @param isDelete
     * @return
     */
    List<DlRescataColumn> findByResourceIdAndIsDelete(Long resourceId, Long isDelete);

    /**
     * 根据资源目录ID删除（多条）记录
     *
     * @param resourceId 资源目录ID
     */
    @Modifying
    @Query("UPDATE DlRescataColumn t SET t.isDelete = :isDelete WHERE t.resourceId = :resourceId")
    void UpdateIsDeleteByResourceId(Long resourceId, Long isDelete);
}
