package com.openjava.datalake.common.responseBody;


import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResp {

    @ApiModelProperty("用户id")
    private Long userid;

    @ApiModelProperty("名称")
    private String fullname;

    @ApiModelProperty("OA-单位组织机构代码")
    private String orgcode;

    @ApiModelProperty("OA-统一认证平台中机构编码")
    private String deptcode;

    @ApiModelProperty("OA-用户uid")
    private String oarelationid;

    @ApiModelProperty("OA-部门ID")
    private String deptid;

    @ApiModelProperty("OA-机构名")
    private String orgname;

    @ApiModelProperty("OA-所在单位科室")
    private String level0;

    @ApiModelProperty("OA-所在单位")
    private String level1;

    @ApiModelProperty("OA-组织机构ID")
    private String orgid;

}
