package com.openjava.datalake.smartapi.vo;

import com.openjava.datalake.rescata.vo.ColumnInfoVo;
import com.openjava.datalake.rescata.vo.TableDataPageVo;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author lwx
 * @date 19/09/29
 * @describe 消息内容
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiTableDataPageVo extends TableDataPageVo implements Serializable {
    private static final long serialVersionUID = -6951952074314360672L;

    @ApiModelProperty("提示消息")
    private List<ApiWarningMessageVo> messageList;

    public ApiTableDataPageVo(List<ColumnInfoVo> columnInfoVoList, List<Object[]> data, Long permissionLevel,
                              int page, int size, Long total, List<ApiWarningMessageVo> messageList) {
        super(columnInfoVoList, data, permissionLevel, page, size, total);
        this.messageList = messageList;
    }

}
