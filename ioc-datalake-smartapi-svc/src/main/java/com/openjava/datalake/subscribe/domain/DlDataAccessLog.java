package com.openjava.datalake.subscribe.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author zhanghan
 * 实体
 * @date 2020/6/10
 */
@ApiModel("资源数据使用日志表")
@Data
@Accessors(chain = true)
@Entity
@Table(name = "DL_DATA_ACCESS_LOG")
@JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
public class DlDataAccessLog implements Serializable, Cloneable {


    @ApiModelProperty("主键")
    @Id
    @Column(name = "ID")
    private Long id;

    @ApiModelProperty("使用者账号")
    @Column(name = "USER_ACCOUNT")
    private String userAccount;

    @ApiModelProperty("使用类型")
    @Column(name = "ACCESS_TYPE")
    private Long accessType;

    @ApiModelProperty("使用设备")
    @Column(name = "USER_AGENT")
    private String userAgent;

    @ApiModelProperty("IP地址")
    @Column(name = "IP_ADDRESS")
    private String ipAddress;

    @ApiModelProperty("使用时间")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Column(name = "DATETIME")
    private Date datetime;

    @ApiModelProperty("查询数量")
    @Column(name = "DATA_AMOUNT")
    private Long dataAmount;

    @ApiModelProperty("查询状态")
    @Column(name = "ACCESS_STATE")
    private Long accessState;

    @ApiModelProperty("请求参数")
    @Column(name = "REQUEST_PARAM")
    private String requestParam;

    @ApiModelProperty("返回参数")
    @Column(name = "RESPONSE_DATA")
    private String responseData;

    @ApiModelProperty("错误信息")
    @Column(name = "ERROR_MSG")
    private String errorMsg;

    @ApiModelProperty("资源编号")
    @Column(name = "RESOURCE_CODE")
    private String resourceCode;

    @ApiModelProperty("订阅字段对象JSON字符串")
    @Column(name = "SUBSCRIBE_COLUMN")
    private String subscribeColumn;
}
