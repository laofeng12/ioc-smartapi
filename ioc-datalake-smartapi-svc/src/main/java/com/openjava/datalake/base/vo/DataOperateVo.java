package com.openjava.datalake.base.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 操作前后的数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataOperateVo implements Serializable {
    private static final long serialVersionUID = -9127137937181775005L;

    @ApiModelProperty("操作前的数据（JSON）")
    private String dataBeforeOperate;

    @ApiModelProperty("操作后的数据（JSON）")
    private String dataAfterOperate;

    public DataOperateVo(String dataBeforeOperate) {
        this.dataBeforeOperate = dataBeforeOperate;
    }
}
