package com.openjava.datalake.rescata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ljdp.component.result.BasicApiResponse;

import java.io.Serializable;
import java.util.List;

/**
 * @Author JiaHai
 * @Description 字段名list + 数据内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDataVo extends BasicApiResponse implements Serializable {
    private static final long serialVersionUID = 6090735946534427739L;

    @ApiModelProperty("字段（列名）信息List")
    private List<ColumnInfoVo> columnInfoVoList;

    @ApiModelProperty("数据（按顺序）")
    private List<Object[]> data;

    @ApiModelProperty("权限等级（1:全部权限/2:部分权限/3:没有权限）")
    private Long permissionLevel;

//    @ApiModelProperty("拥有字段的全部权限的情况（dl.resource.fullpermit.level）0一个拥有全部权限的字段都没有，1至少拥有一个字段有全部权限，2全部字段都有字段的全部权限")
//    private Long fullPermitLevel;

    public TableDataVo(List<ColumnInfoVo> columnInfoVoList, List<Object[]> data) {
        this.columnInfoVoList = columnInfoVoList;
        this.data = data;
    }

    public TableDataVo(Integer code, String message) {
        super(code, message);
    }
}
