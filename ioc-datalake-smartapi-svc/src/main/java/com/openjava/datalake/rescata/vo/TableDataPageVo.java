package com.openjava.datalake.rescata.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author JiaHai
 * @Description 字段名list + 分页内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableDataPageVo extends TableDataVo implements Serializable {
    private static final long serialVersionUID = 6090735946534427739L;

    @ApiModelProperty("当前页数（0开始）")
    private int page;
    @ApiModelProperty("每页条数")
    private int size;
    @ApiModelProperty("总数量")
    private Long total;

    public TableDataPageVo(List<ColumnInfoVo> columnInfoVoList, List<Object[]> data, Long permissionLevel, int page, int size, Long total) {
        super(columnInfoVoList, data, permissionLevel);
        this.page = page;
        this.size = size;
        this.total = total;
    }

    public TableDataPageVo(Integer code, String message) {
        super(code, message);
    }
}
