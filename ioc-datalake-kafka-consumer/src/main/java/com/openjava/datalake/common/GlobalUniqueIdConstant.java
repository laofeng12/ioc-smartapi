package com.openjava.datalake.common;

/**
 * @Author JiaHai
 * @Description 全局唯一ID 常量类
 */
public class GlobalUniqueIdConstant {
    // 使用全局唯一ID时
    /**
     * 全局唯一ID接口调用参数 —— 资源目录ID
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_ID = 2L;
    /**
     * 全局唯一ID接口调用参数 —— 数据库建表的表名组成部分
     */
    public static final Long GLOBAL_UNIQUE_TABLE_ID = 3L;
    /**
     * 全局唯一ID接口调用参数 —— 资源目录信息编码组成部分
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_CODE_ID = 4L;

    /**
     * 全局唯一ID接口调用参数 —— 信息项ID
     */
    public static final Long GLOBAL_UNIQUE_COLUMN_ID = 5L;

    /**
     * 全局唯一ID接口调用参数 —— 资源分类关系表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_CLASSIFICATION_ID = 6L;
    /**
     * 全局唯一ID接口调用参数 —— 资源统计信息表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_STATISTICS_ID = 7L;
    /**
     * 全局唯一ID接口调用参数 —— 资源质量信息表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_ID = 8L;

    /**
     * 全局唯一ID接口调用参数 —— 资源变更详情日志（信息项） 主键ID
     */
    public static final Long GLOBAL_UNIQUE_COLUMN_LOG_ID = 9L;
    /**
     * 全局唯一ID接口调用参数 —— 资源变更详情日志（资源目录） 主键ID
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_LOG_ID = 10L;
    /**
     * 全局唯一ID接口调用参数 —— 资源变更记录表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_CHANGE_ID = 11L;
    /**
     * 全局唯一ID接口调用参数 —— 资源目录与数据库连接关系表
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_DB_REFERENCE_ID = 12L;

    /**
     * 数据湖系统 角色表主键ID
     */
    public static final Long GLOBAL_UNIQUE_ROLE_ID = 13L;
    /**
     * 数据湖系统 菜单信息表主键ID
     */
    public static final Long GLOBAL_UNIQUE_MENU_ID = 14L;
    /**
     * 数据湖系统 角色菜单关系表主键ID
     */
    public static final Long GLOBAL_UNIQUE_ROLE_MENU_ID = 15L;
    /**
     * 数据湖系统 用户角色机构关系表主键ID
     */
    public static final Long GLOBAL_UNIQUE_USER_ORGANIZATION_ROLE_ID = 16L;
    /**
     * 数据湖系统 机构关系表主键ID
     */
    public static final Long GLOBAL_UNIQUE_ORGANIZATION_RELATION_ID = 17L;

    /**
     * 数据湖系统 角色类型表主键ID
     */
    public static final Long GLOBAL_UNIQUE_ROLE_TYPE_ID = 18L;

    /**
     * 总工单表id序列key
     */
    public static final Long GLOBAL_UNIQUE_WORK_ORDER_ID = 19L;
    /**
     * 工单流转记录表id序列key
     */
    public static final Long GLOBAL_UNIQUE_WORK_ORDER_TRANSFER_ID = 20L;
    /**
     * 工单接收方分配表id序列key
     */
    public static final Long GLOBAL_UNIQUE_WORK_ORDER_ACCEPTOR_ID = 21L;
    /**
     * 质量报告工单表id序列key
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_REPORT_ID = 22L;

    /**
     * 数据湖系统 角色申请表主键ID
     */
    public static final Long GLOBAL_UNIQUE_ROLE_APPLY_ID = 23L;

    /**
     * 数据湖系统 合作单位申请表主键ID
     */
    public static final Long GLOBAL_UNIQUE_COOPERATION_ORGANIZATION_APPLY_ID = 24L;

    /**
     * 数据湖的质量报告主键ID
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_REPORT_KEY_ID = 25L;

    /**
     * 数据湖的反馈记录主键ID
     */
    public static final Long GLOBAL_UNIQUE_FEEDBACK_REPORT_ID = 26L;

    /**
     * 质量报告工单轨迹表id序列key
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_TRACK_ID = 27L;

    /**
     * 数据汇聚 信息项主键ID
     */
    public static final Long GLOBAL_UNIQUE_CONVERGE_COLUMN_ID = 30L;
    /**
     * 数据汇聚 权责清单主键ID
     */
    public static final Long GLOBAL_UNIQUE_RIGHT_LIABILITIES_LIST_ID = 31L;
    /**
     * 数据汇聚 项目汇聚主键ID
     */
    public static final Long GLOBAL_UNIQUE_PROJECT_CONVERGE_ID = 32L;
    /**
     * 数据汇聚 项目汇聚-数据需求主键ID
     */
    public static final Long GLOBAL_UNIQUE_PROJECT_DATA_DEMAND_ID = 33L;
    /**
     * 数据汇聚 公用目录主键ID
     */
    public static final Long GLOBAL_UNIQUE_PUBLIC_CATALOG_ID = 34L;
    /**
     * 数据汇聚 目录汇聚 && 数据集汇聚 主键ID
     */
    public static final Long GLOBAL_UNIQUE_CATALOG_DATASET_ID = 35L;
    /**
     * 数据汇聚 项目-部门关联表ID
     */
    public static final Long GLOBAL_UNIQUE_CONVERGE_DEPT_PROJ_ID = 36L;
    /**
     * 数据汇聚 公用目录-部门关联表ID
     */
    public static final Long GLOBAL_UNIQUE_CONVERGE_CATALOG_DEPT = 37L;

    /**
     * 数据湖系统 资源目录非结构化文件关系表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_UNSTRUCTURED_FILE_ID = 38L;

    /**
     * 数据湖资源目录与同步任务关联表主键ID
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_SYNC_TASK_ID = 39L;

    /**
     * 数据湖资源目录关联数据源（挂载）（关联汇聚）主键ID
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_DATASOURCE_ID = 40L;

    // 库表管理模块所有表id
    public static final Long GLOBAL_UNIQUE_CATEGORY_BASICS_SPECIAL_THEME_ID = 41L;

    // API提交接口ID
    public static final Long GLOBAL_UNIQUE_API_PUSH_ID = 42L;

    /**
     * 质量管理数据目录表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_RESOURCE_KEY_ID = 43L;

    /**
     * 质量报告评分规则表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_ASSESS_RULE_KEY_ID = 44L;

    /**
     * 质量报告校验统计结果表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_VALIDATE_RESULT_KEY_ID = 45L;

    /**
     * 质量报告校验作业记录表
     */
    public static final Long GLOBAL_UNIQUE_QUALITY_VALIDATE_RECORD_KEY_ID = 46L;

    // 订阅工单表ID
    public static final Long GLOBAL_UNIQUE_SUBSCRIBE_FORM_ID = 47L;

    // 订阅工单信息项ID
    public static final Long GLOBAL_UNIQUE_SUBSCRIBE_COLUMN_ID = 48L;

    // 资源目录评价
    public static final Long GLOBAL_UNIQUE_SUBSCRIBE_REMARK_ID = 49L;
    // api查询管理
    public static final Long GLOBAL_UNIQUE_API_QUERY_ID = 50L;
    // api查询管理请求参数id
    public static final Long GLOBAL_UNIQUE_API_REQUEST_ID = 51L;
    // api查询管理返回参数id
    public static final Long GLOBAL_UNIQUE_API_RESPONSE_ID = 52L;
    // 资源需求模块所有表id
    public static final Long GLOBAL_UNIQUE_DEMAND_FORM_REPLY_ID = 53L;
    // 目录权限表id
    public static final Long GLOBAL_UNIQUE_PERMISSION_RESOURCE_ID = 54L;
    // 目录信息项权限表id
    public static final Long GLOBAL_UNIQUE_PERMISSION_RESOURCE_COLUMN_ID = 55L;

    /**
     * 全局唯一ID接口调用参数 —— 数据湖关键字表主键ID
     */
    public static final Long GLOBAL_UNIQUE_KEYWORD_ID = 56L;

    /**
     * 全局唯一ID接口调用参数 —— 数据湖资源目录关键字关联表主键ID
     */
    public static final Long GLOBAL_UNIQUE_RESOURCE_KEYWORD_REL_ID = 57L;

    /**
     * 全局唯一ID接口调用参数 —— 数据湖全局图谱节点关联表主键ID
     */
    public static final Long GLOBAL_UNIQUE_GLOBALGRAPH_NODE_REL_ID = 58L;

    // API提交接口日志记录ID
    public static final Long GLOBAL_UNIQUE_API_PUSH_LOG_ID = 59L;

    /**
     * API提交接口监听表Id
     */
    public static final Long GLOBAL_UNIQUE_API_PUSH_LISTEN_ID = 60L;
    /**
     * 数据实时交换接口审计日志
     */
    public static final Long GLOBAL_UNIQUE_API_PUSH_LISTEN_AUDIT_ID = 61L;

    /**
     * 脱敏库管理
     */
    public static final Long GLOBAL_UNIQUE_INSENSITIVE_RULE_ID = 62L;

    /**
     * 需求协作-需求工单ID
     */
    public static final Long GLOBAL_UNIQUE_REQUIRE_WORK_ORDER_ID = 63L;

    /**
     * 需求协作-工单流转记录ID
     */
    public static final Long GLOBAL_UNIQUE_ATO_FLOW_RECORD_ID = 64L;

    /**
     * 信息项历史缓存表 主键ID
     */
    public static final Long GLOBAL_UNIQUE_COLUMN_HISTORY_ID = 65L;

    /**
     * 目录类型表 主键ID
     */
    public static final Long RESOURCE_TYPE_ID = 66L;

    /**
     * 目录类型明细表 主键ID
     */
    public static final Long RESOURCE_TYPE_DETAIL_ID = 67L;

    /**
     * 需求协作-催办ID
     */
    public static final Long GLOBAL_UNIQUE_REMIND_ID = 68L;

    /**
     * 协作工单表ID
     */
    public static final Long GLOBAL_UNIQUE_ASSIST_TASK_ORDER_ID = 69L;

    /**
     * 数据挂载任务ID
     */
    public static final Long RES_DATA_PROVIDE_ID = 70L;

    /**
     * 需求协作-流转记录和资源表关联ID
     */
    public static final Long GLOBAL_UNIQUE_REMIND_RECORD_RESOURCE_REL_ID = 71L;

    /**
     * 数据协作-流程ID
     */
    public static final Long GLOBAL_UNIQUE_FLOW_ID = 72L;

    /**
     * 数据协作-任务ID
     */
    public static final Long GLOBAL_UNIQUE_TASK_ID = 73L;

}
