package com.openjava.datalake.rescata.vo;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author JiaHai
 * @Description
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableData extends TableDataVo implements Serializable {
    private static final long serialVersionUID = 6090735946534427739L;

    @ApiModelProperty("dlRescataColumnList信息")
    private List<DlRescataColumn> dlRescataColumnList;
    private List<DlRescataStrucPermi> structurePermissions;

    public TableData(List<ColumnInfoVo> columnInfoVoList, List<Object[]> data, List<DlRescataColumn> dlRescataColumnList) {
        super(columnInfoVoList, data);
        this.dlRescataColumnList = dlRescataColumnList;
    }

    public TableData(List<ColumnInfoVo> columnInfoVoList){
        super(columnInfoVoList, null);
    }
}
