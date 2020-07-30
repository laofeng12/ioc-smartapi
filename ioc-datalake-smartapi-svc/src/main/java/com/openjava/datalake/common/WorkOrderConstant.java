package com.openjava.datalake.common;

/**
 * @Author xjd
 * @Date 2019/10/24 16:36
 * @Version 1.0
 */
public class WorkOrderConstant {

    // 工单状态(dl.workorder.state)（0已关闭，1未处理，2处理中，3已分发，4已完成，5已驳回）
    public static final Long WORK_ORDER_STAET_CLOSE = 0L;
    public static final Long WORK_ORDER_STAET_UNDEAL = 1L;
    public static final Long WORK_ORDER_STAET_DEALING = 2L;
    public static final Long WORK_ORDER_STAET_DISTRIBUTE = 3L;
    public static final Long WORK_ORDER_STAET_FINISH = 4L;
    public static final Long WORK_ORDER_STAET_REJECT = 5L;

    // 工单审批状态(dl.workorder.approve.state) （1审批通过、2审批不通过）
    public static final Long WORK_ORDER_APPROVE_STAET_PASS = 1L;
    public static final Long WORK_ORDER_APPROVE_STAET_NOTPASS = 2L;

    // 流转状态(dl.transfer.state)（1已处理，2待处理）名称
    public static final Long TRANSF_STAET_DONE = 1L;
    public static final Long TRANSF_STAET_UNDEAL = 2L;

    // 流转操作(dl.transfer.operation)（0关闭工单；1创建工单；2 接受工单；3分发工单；4处理工单；5确认完结；6驳回请求;7办结）
    public static final Long TRANSFER_OP_CLOSE = 0L;
    public static final Long TRANSFER_OP_CREATE = 1L;
    public static final Long TRANSFER_OP_ACCEPT = 2L;
    public static final Long TRANSFER_OP_DISTRIBUTE = 3L;
    public static final Long TRANSFER_OP_DOING = 4L;
    public static final Long TRANSFER_OP_ACOK = 5L;
    public static final Long TRANSFER_OP_REJECT = 6L;
    public static final Long TRANSFER_OP_FINISH = 7L;

    // 指派方式(dl.transfer.assign.type)（1角色，2角色和机构，3账号）
    public static final Long TRANSFER_ASSIGN_ROL = 1L;
    public static final Long TRANSFER_ASSIGN_ROL_ORG = 2L;
    public static final Long TRANSFER_ASSIGN_ACCOUNT = 3L;


}
