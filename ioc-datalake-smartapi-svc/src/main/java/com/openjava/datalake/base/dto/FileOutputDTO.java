package com.openjava.datalake.base.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 文件 输出对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FileOutputDTO implements Serializable {
    private static final long serialVersionUID = 1088838508097752978L;

    private Long uid;

    @ApiModelProperty("文件名")
    private String name;

    @ApiModelProperty("预览URL")
    private String viewUrl;

    @ApiModelProperty("下载URL")
    private String downloadUrl;
}
