package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlRescataColumn;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录结构表业务层接口
 */
public interface DlRescataColumnService {

    /**
     * 根据资源目录resourceId查询
     *
     * @param resourceId 资源目录ID
     * @return
     */
    List<DlRescataColumn> findByResourceId(Long resourceId);
}
