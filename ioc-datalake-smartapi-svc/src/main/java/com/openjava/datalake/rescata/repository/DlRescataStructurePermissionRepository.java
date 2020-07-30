package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import org.ljdp.core.spring.data.DynamicJpaRepository;

import java.util.List;
import java.util.Set;

/**
 * 资源目录字段权限表数据库访问层
 * @author xjd
 *
 */
public interface DlRescataStructurePermissionRepository extends DynamicJpaRepository<DlRescataStrucPermi, Long>, DlRescataStructurePermissionRepositoryCustom{

    /**
     * 	不要用resourceCode，有可能会查出历史版本的字段
     * @param resourceCode
     * @param userAccount
     * @return
     */
    List<DlRescataStrucPermi> findByResourceCodeAndOwnerAccount(String resourceCode, String userAccount);

    List<DlRescataStrucPermi> findByStructureIdInAndOwnerAccount(List<Long> structureIds, String userAccount);
    List<DlRescataStrucPermi> findByStructureIdInAndOwnerAccount(Set<Long> structureIds, String userAccount);
}
