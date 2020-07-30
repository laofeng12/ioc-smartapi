package com.openjava.datalake.rescata.service;


/**
 * @Author JiaHai
 * @Description 数据字典业务层接口
 */
public interface DataCodeService {

    /**
     * 根据 数据字典、字段code，从JVM缓冲中 查询字段名称
     *
     * @param codetype
     * @param code
     * @return
     */
    String findCodeNameByCodetypeAndCodeFromCache(String codetype, Long code);
}
