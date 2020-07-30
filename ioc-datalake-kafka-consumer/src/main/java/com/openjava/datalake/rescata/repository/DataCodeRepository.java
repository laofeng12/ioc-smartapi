package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DataCode;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author JiaHai
 * @Description TODO
 */
public interface DataCodeRepository extends DynamicJpaRepository<DataCode, Long> {
    /**
     * 根据 字典类型 查询
     *
     * @param codetype 字典类型
     * @return
     */
    List<DataCode> findByCodetype(String codetype);

    /**
     * 根据 字典类型 前缀模糊匹配
     *
     * @param codetype
     * @return
     */
    List<DataCode> findByCodetypeLike(String codetype);

    /**
     * 根据 数据字典、字段code 查询字段名称
     *
     * @param codetype
     * @param code
     * @return
     */
    @Query("SELECT d.codename FROM DataCode d WHERE d.codetype = :codetype AND d.code = :code")
    String findCodeNameByCodetypeAndCode(String codetype, String code);

}
