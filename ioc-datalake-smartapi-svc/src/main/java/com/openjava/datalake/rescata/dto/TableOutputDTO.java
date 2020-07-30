package com.openjava.datalake.rescata.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 数据库表信息 输出对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableOutputDTO implements Serializable {
    private static final long serialVersionUID = -7251401821235575948L;

    @ApiModelProperty("表名（存储标识）")
    private String resourceTableName;

    @ApiModelProperty("表注释（资源目录名称）")
    private String resourceName;
}
