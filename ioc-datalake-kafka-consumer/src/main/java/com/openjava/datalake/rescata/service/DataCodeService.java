package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DataCode;
import com.openjava.datalake.rescata.vo.DataCodeVo;

import java.util.List;
import java.util.Map;

/**
 * @Author JiaHai
 * @Description 数据字典业务层接口
 */
public interface DataCodeService {
    /**
     * 根据 字典类型 查询
     *
     * @param codetype 字典类型
     * @return
     */
    List<DataCode> findByCodetype(String codetype);

    /**
     * 根据 字典类型 查询
     *
     * @param codetype 字典类型
     * @return 返回VO
     */
    List<DataCodeVo> findVoByCodetype(String codetype);

    Map<String, String> findByCodetypeToMap(String codetype);


    /**
     * 根据 数据字典、字段code，从JVM缓冲中 查询字段名称
     *
     * @param codetype
     * @param code
     * @return
     */
    String findCodeNameByCodetypeAndCodeFromCache(String codetype, Long code);

    Map<String, DataCode> getCodeMap(String s);

    /**
     * 根据字典类型模糊查询
     *
     * @param codetype 字典类型
     * @return
     */
    List<DataCode> findByCodetypeLike(String codetype);

    /**
     * 初始化 数据湖 数据字典
     *
     * @return
     */
    void initDataDictionaryOfDataLake();

    /**
     * 刷新 数据湖 数据字典
     */
    void refreshDataDictionaryOfDataLake();
}
