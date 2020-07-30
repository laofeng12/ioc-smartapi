package com.openjava.datalake.rescata.service;

import org.ljdp.component.exception.APIException;

/**
 * @Author JiaHai
 * @Description 获取全局唯一ID 业务层接口
 */
public interface GlobalUniqueIdService {
    /**
     * 根据TSYS_SEQUENCE表的主键sequenceId，获取全局唯一ID
     *
     * @param sequenceId
     * @return
     * @throws APIException
     */
    Long getGlobalUniqueIdById(Long sequenceId) throws APIException;
}
