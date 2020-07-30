package com.openjava.datalake.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 资源目录非结构化文件输出对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ResourceFileOutputDTO extends FileOutputDTO implements Serializable {
    private static final long serialVersionUID = 3022146176393772647L;

    @ApiModelProperty("主键,非结构化信息项ID，unstructureId")
    private Long id;
    @ApiModelProperty("公开范围String")
    private String openScopeString;
    @ApiModelProperty("上传人")
    private String uploadUser;
    @ApiModelProperty("文件类型")
    private String fileType;
    @ApiModelProperty("文件大小")
    private String fileSize;
    @ApiModelProperty("上传时间")
    private String uploadTime;
    
}
