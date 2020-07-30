package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlRescataResourcePermission;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * 资源目录权限表数据库访问层
 * @author xjd
 *
 */
public interface DlRescataResourcePermissionRepository extends DynamicJpaRepository<DlRescataResourcePermission, Long>, DlRescataResourcePermissionRepositoryCustom{

    Page<DlRescataResourcePermission> findByOwnerAccount(String userAccount, Pageable pageable);

    List<DlRescataResourcePermission> findByResourceCodeAndOwnerAccount(String resourceCode, String userAccount);

    DlRescataResourcePermission findByResourceIdAndOwnerAccount(Long resourceId, String userAccount);

    @Query(value =
            " select * from (select a.*,rownum rn from ( "+
                "select t.deptTopId, t.topDeptName, t.latelyTime, t.usedCount from " +
                        " (select p.owner_dept_top_id as deptTopId, p.owner_dept_top_name as topDeptName, max(p.create_time) as latelyTime, count(1) as usedCount " +
                        " from dl_rescata_resource_permission p " +
                        " where p.resource_code = :resourceCode and p.permission_Level in (1, 2) " +
                        " group by p.owner_dept_top_id, p.owner_dept_top_name) t " +
                        " where 1 = 1 " +
                        " and (to_char(t.latelyTime, 'yyyymmddhh24miss') <= :endTimeString OR :endTimeString IS NULL) " +
                        " and (to_char(t.latelyTime, 'yyyymmddhh24miss') >= :startTimeString OR :startTimeString IS NULL) " +
                    " order by t.usedCount desc " +  // 可以在order by 后面直接写order by的sql，也可以在@PageableDefault里面写排序，但是 pageable 里面不能加sort ，也可以用 ?#{#pageable} 但是其他地方的变量就也要用?1 ?2 ...
            " ) a where " +
            " rownum <= " +
            " :pageEndRow "+
            " ) where " +
            " rn > " +
            " :pageStartRow  " +
                    " /* #pageable# */ ", // 这种跟最后面就行不用写order by 不过也是不能在pageable里面加sort ,要在pageable里面加sort
            //order by :#{#pageable} 可以在order by 后面直接写order by的sql，也可以在@PageableDefault里面写排序，但是 pageable 里面不能加sort ，也可以用 ?#{#pageable} 但是其他地方的变量就也要用?1 ?2 ...
            countQuery =
            "select count(1) from (" +
                    "select t.deptTopId, t.topDeptName, t.latelyTime, t.usedCount from " +
                    " (select p.owner_dept_top_id as deptTopId, p.owner_dept_top_name as topDeptName, max(p.create_time) as latelyTime, count(1) as usedCount " +
                    " from dl_rescata_resource_permission p " +
                    " where p.resource_code = :resourceCode and p.permission_Level in (1, 2) " +
                    " group by p.owner_dept_top_id, p.owner_dept_top_name) t " +
                    " where 1 = 1 " +
                    " and (to_char(t.latelyTime, 'yyyymmddhh24miss') <= :endTimeString OR :endTimeString IS NULL) " +
                    " and (to_char(t.latelyTime, 'yyyymmddhh24miss') >= :startTimeString OR :startTimeString IS NULL) " +
                    ") " ,
            nativeQuery = true)
    Page<Object[]> countUsedTopDeptByResourceCode(@Param("resourceCode") String resourceCode,
                                                  @Param("startTimeString") String startTimeString,
                                                  @Param("endTimeString") String endTimeString,
                                                  @Param("pageEndRow") int pageEndRow,
                                                  @Param("pageStartRow") int pageStartRow,
                                                  Pageable pageable);

    @Modifying
    @Query(value = "delete from DlRescataResourcePermission where ownerAccount = :ownerAccount and resourceCode = :resourceCode")
    int deleteByOwnerAccountAndResourceCode(String ownerAccount, String resourceCode);


    /**
     * 由于没有权限的也会存到DlRescataResourcePermission，所以要加条件 permissionLevel in (1, 2)
     * @param userAccount
     * @param resourceCodes
     * @param pageable
     * @return
     */
    @Deprecated
    Page<DlRescataResourcePermission> findByOwnerAccountAndResourceCodeIn(String userAccount, List<String> resourceCodes, Pageable pageable);

    Page<DlRescataResourcePermission> findByOwnerAccountAndPermissionLevelInAndResourceCodeIn(String userAccount, List<Long> permittedState, List<String> resourceCodes, Pageable pageable);

    /**
     * （UI首页）使用单位数
     * @return
     */
    @Query("SELECT COUNT(DISTINCT p.ownerDeptTopId) FROM DlRescataResourcePermission p")
    Long countDistinctOwnerDeptTopId();


    @Modifying
    @Query("update DlRescataResourcePermission set permissionLevel = :permissionLevel where resourceCode = :resourceCode and ownerAccount in (:ownerAccounts)")
    int updatePermissionLevelByResourceCodeAndOwnerAccountIn(Long permissionLevel, String resourceCode, Set<String> ownerAccounts);


    Page<DlRescataResourcePermission> findByResourceId(Pageable pageable, Long resourceId);

    List<DlRescataResourcePermission> findByResourceId(Long resourceId);

    List<DlRescataResourcePermission> findByResourceCode(String resourceCode);
}

