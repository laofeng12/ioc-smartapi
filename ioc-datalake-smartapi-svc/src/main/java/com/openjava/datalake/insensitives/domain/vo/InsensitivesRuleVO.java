package com.openjava.datalake.insensitives.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author xjd
 * @Date 2019/7/12 14:42
 * @Version 1.0
 */
@ApiModel("脱敏规则下拉选择")
@Data
@AllArgsConstructor
public class InsensitivesRuleVO {

    @ApiModelProperty("脱敏规则ID")
    private Long insensitivesRuleId;
    @ApiModelProperty(value = "规则类型(dl.insensitives.rule.type )（1.字符集;2.规则库）", required = true)
    private Long ruleType;
    @ApiModelProperty(value = "规则名称", required = true)
    private String ruleName;
    @ApiModelProperty("规则描述")
    private String ruleDesc;
    @ApiModelProperty(value = "左/中/右类型", hidden = false)
    private String paramType;

}
