package com.openjava.datalake.common.gateway;

import com.openjava.datalake.smartapi.domain.DlApiQuery;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author xjd
 * @Date 2020/2/11 16:23
 * @Version 1.0
 */
@Data
public class GatewayDeptInfo {

    @ApiModelProperty(value = "接口APIid", hidden = true)
    private String customApiId;
    @ApiModelProperty(value = "接口类型（查询API接口=1，提交API接口类型=9）", hidden = true)
    private String moduleType;
    @ApiModelProperty(value = "创建人机构编码",hidden = true)
    private String deptCode;
    @ApiModelProperty(value = "创建人机构名称",hidden = true)
    private String deptName;
    @ApiModelProperty(value = "创建人顶级机构编码",hidden = true)
    private String topDeptCode;
    @ApiModelProperty(value = "创建人顶级机构名称",hidden = true)
    private String topDeptName;

    public GatewayDeptInfo(DlApiQuery dlApiQuery) {
        this.customApiId = String.valueOf(dlApiQuery.getQueryId());
        this.moduleType = GatewayConstant.MODULE_TYPE_QUERY.toString();
//        this.deptCode = dlApiQuery.getCreateDeptCode();
//        this.deptName = dlApiQuery.getCreateDeptName();
//        this.topDeptCode = dlApiQuery.getCreateTopDeptCode();
//        this.topDeptName = dlApiQuery.getCreateTopDeptName();
    }
}
