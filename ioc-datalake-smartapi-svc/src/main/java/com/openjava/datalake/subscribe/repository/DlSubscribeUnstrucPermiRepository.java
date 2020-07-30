package com.openjava.datalake.subscribe.repository;

import com.openjava.datalake.subscribe.domain.DlSubscribeUnstrucPermi;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author xjd
 * @Date 2019/12/16 20:45
 * @Version 1.0
 */
public interface DlSubscribeUnstrucPermiRepository extends DynamicJpaRepository<DlSubscribeUnstrucPermi, Long> {

    List<DlSubscribeUnstrucPermi> findByResourceIdAndOwnerAccount(Long resourceId, String userAccount);
    List<DlSubscribeUnstrucPermi> findByResourceCodeAndOwnerAccount(String resourceCode, String userAccount);

    @Modifying
    @Query("delete from DlSubscribeUnstrucPermi where resourceCode = :resourceCode and ownerAccount = :ownerAccount ")
    int deleteByResourceCodeAndOwnerAccount(String resourceCode, String ownerAccount);

    List<DlSubscribeUnstrucPermi> findByResourceCode(String resourceCode);
}
