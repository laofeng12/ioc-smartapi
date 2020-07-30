package com.openjava.datalake.rescata.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description 资源目录结构表
 */
@Entity
@Table(name = "DL_RESCATA_COLUMN")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class DlRescataColumn implements Serializable, Cloneable {
    private static final long serialVersionUID = -4926976973146202542L;

    @Id
    @ApiModelProperty("资源目录结构表ID")
    private Long structureId;
    @ApiModelProperty("上一版本资源目录结构表ID")
    private Long oldStructureId;

    @ApiModelProperty("资源目录宽表ID")
    private Long resourceId;
    @ApiModelProperty("是否主键（1是、0否）")
    private Long isPrimaryKey;
    @ApiModelProperty("字段Code")
    private Long columnCode;
    @ApiModelProperty("字段名称")
    private String columnName;
    @ApiModelProperty("字段注释")
    private String columnComment;
    @Deprecated
    @ApiModelProperty("字段公开属性（dl.column.open.scope）（1对外公开、2对内公开、3不公开）")
    private Long columnOpenScope;
    @ApiModelProperty("字段定义")
    private String columnDefinition;
    @ApiModelProperty("数据类型（dl.column.datatype）（1短字符、2较长字符、3长字符、4日期型、5整数型、6小数型）")
    private Long dataType;
    @ApiModelProperty("字段长度")
    private Long columnLength;
    @ApiModelProperty("小数位数")
    private Long decimalLength;
    @ApiModelProperty("计量单位")
    private String measurementUnit;
    @ApiModelProperty("数据格式")
    private String dataFormat;
    @ApiModelProperty("质量规则")
    private String qualityRule;

    @ApiModelProperty("是否必填（public.YN）（1有效、0无效）")
    private Long isRequire;

    @ApiModelProperty("是否加密（public.YN）（1有效、0无效）")
    private Long isEncrypt;
    @ApiModelProperty("加密说明")
    private String encryptDescription;

    @ApiModelProperty("是否脱敏（public.YN）（1有效、0无效）")
    private Long isDesensitization;
    @ApiModelProperty("脱敏规则ID")
    private Long insensitivesRuleId;
    @ApiModelProperty("脱敏规则（中文）")
    private String desensitizationRule;
    @ApiModelProperty("脱敏开始与结束位置")
    private String insensitivesStartEnd;
    @ApiModelProperty("脱敏说明")
    private String insensitivesRuleDescription;

    @ApiModelProperty("是否用于列表（public.YN）（1有效、0无效）")
    private Long isList;
    @ApiModelProperty("是否用于查询（public.YN）（1有效、0无效）")
    private Long isQuery;
    @ApiModelProperty("是否用于导出（public.YN）（1有效、0无效）")
    private Long isExport;
    @ApiModelProperty("是否用于查看（public.YN）（1有效、0无效）")
    private Long isShow;

    @ApiModelProperty("填写方式")
    private String writeType;
    @ApiModelProperty("代码集")
    private String codeSet;
    @ApiModelProperty("默认值")
    private String defaultValue;
    @ApiModelProperty("信息项说明")
    private String infoDescription;
    @ApiModelProperty("状态（public.YN）（1有效、0无效）")
    private Long isDeleted;
    @ApiModelProperty("签名值")
    private String sign;

    @Transient
    @ApiModelProperty("脱敏开始位置")
    private Long insensitivesStartIndex;
    @Transient
    @ApiModelProperty("脱敏结束位置")
    private Long insensitivesEndIndex;
}
