package com.openjava.datalake.insensitives.repository;

import com.openjava.datalake.insensitives.domain.DlInsensitivesRule;
import com.openjava.datalake.insensitives.domain.vo.InsensitivesRuleVO;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * 脱敏规则库数据库访问层
 * @author xjd
 *
 */
public interface DlInsensitivesRuleRepository extends DynamicJpaRepository<DlInsensitivesRule, Long>, DlInsensitivesRuleRepositoryCustom{
    @Modifying
    @Query(value = "update DlInsensitivesRule set isDelete = 1 where insensitivesRuleId = :id")
    int updataIsDeleteById(Long id);

    @Modifying
    @Query(value = "update DlInsensitivesRule set isDelete = 1 where insensitivesRuleId in (:ids)")
    int updataIsDeleteByIdIn(String ids);

    @Query(value = "select new com.openjava.datalake.insensitives.domain.vo.InsensitivesRuleVO(insensitivesRuleId, ruleType, ruleName, ruleDesc, paramType) " +
            "from DlInsensitivesRule where " +
            "(ruleType = :ruleType or :ruleType is null) " +
            "and state = :enable and isDelete <> :deleted ")
    List<InsensitivesRuleVO> findByRuleTypeEnable(Long ruleType, Long enable, Long deleted);
}
