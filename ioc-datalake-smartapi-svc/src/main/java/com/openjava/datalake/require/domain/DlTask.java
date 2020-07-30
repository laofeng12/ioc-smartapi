package com.openjava.datalake.require.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.openjava.admin.user.vo.OaOrgVO;
import com.openjava.admin.user.vo.OaUserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.ljdp.secure.valid.AddGroup;
import org.ljdp.secure.valid.UpdateGroup;
import org.springframework.data.domain.Persistable;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * 实体
 *
 * @author xmk
 */
@ApiModel("任务")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Entity
@Table(name = "DL_TASK")
public class DlTask implements Persistable<Long>, Serializable {

    @ApiModelProperty("任务ID")
    @Id
    @Column(name = "ID")
    private Long id;

    @ApiModelProperty("父任务ID")
    @Max(value = 999999999999999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "PID")
    private Long pid;

    @ApiModelProperty("流程ID")
    @Max(value = 999999999999999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "FLOW_ID")
    private Long flowId;

    @ApiModelProperty("业务ID")
    @Max(value = 999999999999999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "BID")
    private Long bid;

    @ApiModelProperty("业务类型")
    @Max(value = 9L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "BTYPE")
    private Long btype;

    @ApiModelProperty("任务编号")
    @Length(min = 0, max = 64, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "NMBER")
    private String nmber;

    @ApiModelProperty("任务名称")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "TASK_NAME")
    private String taskName;

    @ApiModelProperty("任务类型")
    @Max(value = 9L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "TASK_TYPE")
    private Long taskType;

    @ApiModelProperty("任务状态（1：未处理 2：处理中 3：已处理）")
    @Max(value = 9L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "STATUS")
    private Long status;

    @ApiModelProperty("发送人ID")
    @Max(value = 999999999999999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_ID")
    private Long senderId;

    @ApiModelProperty("发送人账号")
    @Length(min = 0, max = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_ACCOUNT")
    private String senderAccount;

    @ApiModelProperty("发送人名称")
    @Length(min = 0, max = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_NAME")
    private String senderName;

    @ApiModelProperty("发送人部门CODE")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_DEPT_CODE")
    private String senderDeptCode;

    @ApiModelProperty("发送人部门名称")
    @Length(min = 0, max = 200, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_DEPT_NAME")
    private String senderDeptName;

    @ApiModelProperty("发送人科室CODE")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_SECTION_CODE")
    private String senderSectionCode;

    @ApiModelProperty("发送人科室名称")
    @Length(min = 0, max = 200, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SENDER_SECTION_NAME")
    private String senderSectionName;

    @ApiModelProperty("发送类型（0:全部 1：部门 2：科室 3：角色  4：个人 5:部门+角色 6：科室+角色 7：部门+个人）")
    @Max(value = 9L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "SEND_TYPE")
    private Long sendType;

    @ApiModelProperty("发送时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "SEND_TIME")
    private LocalDateTime sendTime;

    @ApiModelProperty("受理人ID")
    @Max(value = 999999999999999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_ID")
    private Long assigneeId;

    @ApiModelProperty("受理人账号")
    @Length(min = 0, max = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_ACCOUNT")
    private String assigneeAccount;

    @ApiModelProperty("受理人名称")
    @Length(min = 0, max = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_NAME")
    private String assigneeName;

    @ApiModelProperty("受理人部门CODE")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_DEPT_CODE")
    private String assigneeDeptCode;

    @ApiModelProperty("受理人部门名称")
    @Length(min = 0, max = 200, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_DEPT_NAME")
    private String assigneeDeptName;

    @ApiModelProperty("受理人科室CODE")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_SECTION_CODE")
    private String assigneeSectionCode;

    @ApiModelProperty("受理人科室名称")
    @Length(min = 0, max = 200, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_SECTION_NAME")
    private String assigneeSectionName;

    @ApiModelProperty("受理人角色别名")
    @Length(min = 0, max = 32, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "ASSIGNEE_ROLE_ALIAS")
    private String assigneeRoleAlias;

    @ApiModelProperty("处理人ID")
    @Max(value = 999999999999999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_ID")
    private Long operatorId;

    @ApiModelProperty("处理人账号")
    @Length(min = 0, max = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_ACCOUNT")
    private String operatorAccount;

    @ApiModelProperty("处理人名称")
    @Length(min = 0, max = 100, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_NAME")
    private String operatorName;

    @ApiModelProperty("处理人部门CODE")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_DEPT_CODE")
    private String operatorDeptCode;

    @ApiModelProperty("处理人科室名称")
    @Length(min = 0, max = 200, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_DEPT_NAME")
    private String operatorDeptName;

    @ApiModelProperty("处理人科室CODE")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_SECTION_CODE")
    private String operatorSectionCode;

    @ApiModelProperty("处理人科室名称")
    @Length(min = 0, max = 200, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_SECTION_NAME")
    private String operatorSectionName;

    @ApiModelProperty("处理人角色别名")
    @Length(min = 0, max = 32, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATOR_ROLE_ALIAS")
    private String operatorRoleAlias;

    @ApiModelProperty("操作时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Column(name = "OPERATE_TIME")
    private LocalDateTime operateTime;

    @ApiModelProperty("处理操作")
    @Max(value = 99L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATE_TYPE")
    private Long operateType;

    @ApiModelProperty("处理内容")
    @Length(min = 0, max = 50, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "OPERATE_DESC")
    private String operateDesc;

    @ApiModelProperty("附言")
    @Length(min = 0, max = 500, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "REMARK")
    private String remark;

    @ApiModelProperty("耗时（秒）")
    @Max(value = 99999999L, groups = {AddGroup.class, UpdateGroup.class})
    @Column(name = "CONSUME_TIME")
    private Long consumeTime;

    @ApiModelProperty("是否新增")
    @JsonIgnore
    @Transient
    private Boolean isNew;

    @Transient
    @JsonIgnore
    @Override
    public Long getId() {
        return this.id;
    }

    @JsonIgnore
    @Transient
    @Override
    public boolean isNew() {
        if (isNew != null) {
            return isNew;
        }
        if (this.id != null) {
            return false;
        }
        return true;
    }

    /**
     * 填充发送信息
     *
     * @param user 用户信息
     */
    public void fillUpSendInfo(OaUserVO user) {
        //获取用户所属的机构信息
        OaOrgVO topOrg = user.getTopOrg();
        if (topOrg != null) {
            //部门ID
            String topOrgDeptcode = topOrg.getDeptcode();
            this.setSenderDeptCode(topOrgDeptcode);
            //部门名称
            String orgName = topOrg.getOrgname();
            this.setSenderDeptName(orgName);

        }
        String userId = user.getUserId();
        if (StringUtils.isNumeric(userId)) {
            //用户ID
            this.setSenderId(Long.valueOf(userId));
        }
        //科室ID
        this.setSenderSectionCode(user.getDeptcode())
                //科室名称
                .setSenderSectionName(user.getOrgname())
                .setSenderAccount(user.getUserAccount())
                .setSenderName(user.getUserName())
//				.setCreateUuid(user.getUserCode())
                .setSendTime(LocalDateTime.now());
    }

    /**
     * 填充操作信息
     *
     * @param user 用户信息
     */
    public void fillUpOperateInfo(OaUserVO user) {
        //获取用户所属的机构信息
        OaOrgVO topOrg = user.getTopOrg();
        if (topOrg != null) {
            //部门ID
            String topOrgDeptcode = topOrg.getDeptcode();
            this.setOperatorDeptCode(topOrgDeptcode);
            //部门名称
            String orgName = topOrg.getOrgname();
            this.setOperatorDeptName(orgName);

        }
        String userId = user.getUserId();
        if (StringUtils.isNumeric(userId)) {
            //用户ID
            this.setOperatorId(Long.valueOf(userId));
        }
        //科室ID
        this.setOperatorSectionCode(user.getDeptcode())
                //科室名称
                .setOperatorSectionName(user.getOrgname())
                .setOperatorAccount(user.getUserAccount())
                .setOperatorName(user.getUserName())
                .setOperateTime(LocalDateTime.now());
        if (user.getRoleAlias() != null && user.getRoleAlias().size() > 0) {
            this.setOperatorRoleAlias(String.join(",", user.getRoleAlias()));
        }
        Duration duration = Duration.between(this.getSendTime(), this.getOperateTime());
        this.setConsumeTime(duration.toMillis() / 1000);
    }

}
