package com.openjava.datalake.smartapi.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author lwx
 * @date 19/09/27
 * @describe 接口返回消息体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiWarningMessageVo implements Serializable {
    private static final long serialVersionUID = 1215268459955210015L;

    @ApiModelProperty("列名")
    private String columnName;
    @ApiModelProperty("消息内容")
    private String message;
}
