package com.openjava.datalake.smartapi.vo;

import com.openjava.datalake.smartapi.domain.DlApiResponse;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ljdp.component.exception.APIException;

import java.io.Serializable;
import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源数据查询包装类（resourceId）
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataQueryParam implements Serializable {
    private static final long serialVersionUID = -1049265298728237924L;

    @ApiModelProperty(value = "资源目录ID", required = true)
    private Long resourceId;

    @ApiModelProperty("资源目录Code")
    private String resourceCode;

    @ApiModelProperty(value = "当前页（0开始）", required = true)
    private int page;
    @ApiModelProperty(value = "每页条数", required = true)
    private int size;

    @ApiModelProperty("需要查询的字段 的ID")
    private List<Long> selectColumnIdList;

    @ApiModelProperty("全文搜索关键字")
    private String search;

    @ApiModelProperty("查询字段List")
    private List<DlApiResponse> selectColumnList;

    @ApiModelProperty("字段参数List")
    private List<ColumnParam> columnParamList;

    @ApiModelProperty("自定义where条件")
    private String selfConditionSql;

    public void addColumnParam(ColumnParam columnParam){
        columnParamList.add(columnParam);
    }
    /**
     * 检查必填参数
     *
     * @param dataQueryParam
     * @return
     * @throws APIException
     */
    public static boolean checkRequire(DataQueryParam dataQueryParam) throws APIException {
        if (null == dataQueryParam) {
            throw new APIException("参数不可为空");
        }
        if (dataQueryParam.getPage() < 0) {
            throw new APIException("当前页数不可小于0");
        }
        if (dataQueryParam.size <= 0) {
            throw new APIException("每页条数需要大于0");
        }
        return true;
    }
}
