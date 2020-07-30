package com.openjava.datalake.rescata.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @Author JiaHai
 * @Description TODO
 */
@Entity
@Table(name = "TSYS_CODE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataCode implements Serializable {
    @Id
    @ApiModelProperty("主键ID")
    private Long codeid;
    @ApiModelProperty("字段code")
    private String code;
    @ApiModelProperty("字典类型")
    private String codetype;
    @ApiModelProperty("字断名称")
    private String codename;
    @ApiModelProperty("字典含义")
    private String codevalue;
}
