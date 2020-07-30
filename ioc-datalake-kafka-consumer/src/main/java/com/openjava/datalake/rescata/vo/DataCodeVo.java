package com.openjava.datalake.rescata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 数据字典VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCodeVo implements Serializable {
    private static final long serialVersionUID = -6719366737005442623L;

    @ApiModelProperty("字段code")
    private Long code;
    @ApiModelProperty("字典类型")
    private String codetype;
    @ApiModelProperty("字断名称")
    private String codename;
    @ApiModelProperty("字典含义")
    private String codevalue;
}
