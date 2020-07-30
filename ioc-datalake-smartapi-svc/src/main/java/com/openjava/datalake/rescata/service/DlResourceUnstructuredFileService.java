package com.openjava.datalake.rescata.service;

import com.openjava.datalake.base.dto.ResourceFileOutputDTO;
import org.ljdp.component.exception.APIException;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录非结构化文件关系表 业务层接口
 */
public interface DlResourceUnstructuredFileService {

    List<ResourceFileOutputDTO> getFileListByResourceCode(String resourceCode) throws APIException;
}
