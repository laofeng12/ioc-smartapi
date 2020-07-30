package com.openjava.datalake.common;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author JiaHai
 * @Description 资源相关常量类
 */
public class ResourceConstant {
    /**
     * 信息资源编码前缀 —— 东莞市
     */
    public static final String DONG_GUAN = "DGS_";
    /**
     * 信息资源编码 —— 归集库
     */
    public static final String DATASOURCE_HJ = "_HJ_";
    /**
     * 信息资源编码 —— 中心库
     */
    public static final String DATASOURCE_ZX = "_ZX_";
    /**
     * 信息资源编码 —— 基础库
     */
    public static final String DATASOURCE_BASE = "_JC_";
    /**
     * 信息资源编码 —— 主题库
     */
    public static final String DATASOURCE_THEME = "_ZhuT_";
    /**
     * 信息资源编码 —— 专题库
     */
    public static final String DATASOURCE_SUBJECT = "_ZhuanT_";


    /**
     * 资源目录分库类别
     */
    public static final String DL_RESOURCE_REPOSITORY_TYPE = "dl.resource.repository.type";
    /**
     * 资源目录分库类别 —— 归集库
     */
    public static final Long REPOSITORY_TYPE_HJ = 1L;
    /**
     * 资源目录分库类别 —— 中心库
     */
    public static final Long REPOSITORY_TYPE_ZX = 2L;
    /**
     * 资源目录分库类别 —— 基础库
     */
    public static final Long REPOSITORY_TYPE_BASE = 3L;
    /**
     * 资源目录分库类别 —— 主题库
     */
    public static final Long REPOSITORY_TYPE_THEME = 4L;
    /**
     * 资源目录分库类别 —— 专题库
     */
    public static final Long REPOSITORY_TYPE_SUBJECT = 5L;
    /**
     * 资源目录分库类别List
     */
    public static final List<Long> REPOSITORY_TYPE_LIST = ImmutableList.of(REPOSITORY_TYPE_HJ, REPOSITORY_TYPE_ZX, REPOSITORY_TYPE_BASE, REPOSITORY_TYPE_THEME, REPOSITORY_TYPE_SUBJECT);

    /**
     * 数据格式
     */
    public static final String DL_RESOURCE_TYPE = "dl.resource.type";
    /**
     * 数据格式 —— 数据库
     */
    public static final Long RESOURCE_TYPE_DATABASE = 1L;
    /**
     * 数据格式 —— 附件
     */
    public static final Long RESOURCE_TYPE_TEXT = 2L;
    /**
     * 数据格式 —— 接口
     */
    public static final Long RESOURCE_TYPE_INTERFACE = 3L;
    /**
     * 数据格式 —— 音频
     */
//    public static final Long RESOURCE_TYPE_AUDIO = 4L;
    /**
     * 数据格式 —— 视频
     */
//    public static final Long RESOURCE_TYPE_VIDEO = 5L;
    /**
     * 所有数据格式（List）
     */
    public static final List<Long> RESOURCE_TYPE_LIST = ImmutableList.of(RESOURCE_TYPE_DATABASE, RESOURCE_TYPE_TEXT,RESOURCE_TYPE_INTERFACE );


    /**
     * 资源共享范围
     */
    public static final String DL_RESOURCE_OPEN_SCOPE = "dl.resource.share.scope";
    /**
     * 资源共享范围 —— 全市共享
     */
    public static final Long RESOURCE_OPEN_SCOPE_PUBLIC = 1L;
    /**
     * 资源共享范围 —— 部门私有
     */
    public static final Long RESOURCE_OPEN_SCOPE_INTERNAL = 2L;
    /**
     * 资源共享范围 —— 个人私有
     */
    public static final Long RESOURCE_OPEN_SCOPE_PRIVATE = 3L;
    /**
     * 资源共享范围List
     */
    public static final List<Long> RESOURCE_OPEN_SCOPE_LIST = ImmutableList.of(RESOURCE_OPEN_SCOPE_PUBLIC, RESOURCE_OPEN_SCOPE_INTERNAL, RESOURCE_OPEN_SCOPE_PRIVATE);

    /**
     * 目录类型可见范围
     */
    public static final String DL_CATALOG_TYPE_USAGE_SCOPE = "dl.catalog.type.usage.scope";
    /**
     * 目录类型可见范围 —— 全市可见
     */
    public static final Long CATALOG_TYPE_USAGE_SCOPE_PUBLIC = 1L;
    /**
     * 目录类型可见范围 —— 部门可见
     */
    public static final Long CATALOG_TYPE_USAGE_SCOPE_INTERNAL = 2L;
    /**
     * 目录类型使用范围 —— 个人使用
     */
    public static final Long CATALOG_TYPE_USAGE_SCOPE_PRIVATE = 3L;
    /**
     * 目录类型使用范围List
     */
    public static final List<Long> CATALOG_TYPE_USAGE_SCOPE_LIST = ImmutableList.of(CATALOG_TYPE_USAGE_SCOPE_PUBLIC, CATALOG_TYPE_USAGE_SCOPE_INTERNAL);


    /**
     * 目录属性（资源公开范围 -> 目录分类） 权责清单
     */
    public static final Long RESOURCE_OPEN_SCOPE_LIABILITY = 5L;
    /**
     * 目录属性（资源公开范围 -> 目录分类）项目汇聚
     */
    public static final Long RESOURCE_OPEN_SCOPE_PROJECT = 6L;
    /**
     * 目录属性（资源公开范围 -> 目录分类）公用目录
     */
    public static final Long RESOURCE_OPEN_SCOPE_PUBLIC_CATALOG = 7L;


    /**
     * 数据提供方式
     */
    public static final String DL_RESOURCE_DATA_PROVIDE_MODE = "dl.resource.data.provide.mode";
    /**
     * 数据提供方式 —— 库表挂载
     */
    public static final Long RESOURCE_DATA_PROVIDE_MODE_PROCESSOR = 1L;
    /**
     * 数据提供方式 —— 附件上传
     */
    public static final Long RESOURCE_DATA_PROVIDE_MODE_ATTACHMENT = 2L;
    /**
     * 数据提供方式 —— 接口获取
     */
    public static final Long RESOURCE_DATA_PROVIDE_MODE_INTERFACE = 3L;
    /**
     * 数据提供方式 —— 数据归集
     */
    public static final Long RESOURCE_DATA_PROVIDE_MODE_COLLECTION = 4L;
    /**
     * 数据提供方式 —— 第三方接口
     */
    public static final Long RESOURCE_DATA_PROVIDE_MODE_THIRDPARTYINTERFACE = 5L;
    /**
     * 数据提供方式（List）
     */
    public static final List<Long> RESOURCE_DATA_PROVIDE_MODE_LIST = ImmutableList.of(RESOURCE_DATA_PROVIDE_MODE_PROCESSOR, RESOURCE_DATA_PROVIDE_MODE_ATTACHMENT, RESOURCE_DATA_PROVIDE_MODE_INTERFACE, RESOURCE_DATA_PROVIDE_MODE_COLLECTION,RESOURCE_DATA_PROVIDE_MODE_THIRDPARTYINTERFACE);


    /**
     * 库表挂载数据库类型 —— Oracle
     */
    public static final int PROCESSOR_ORACLE = 0;
    /**
     * 库表挂载数据库类型 —— MySQL
     */
    public static final int PROCESSOR_MYSQL = 1;
    /**
     * 库表挂载数据库类型 —— 数据湖（PostgreSQL）
     */
    public static final int PROCESSOR_DATA_LAKE = 2;
    /**
     * 库表挂载可选数据库类型（List）
     */
    public static final List<Integer> PROCESSOR_DATABASE_TYPE_LIST = ImmutableList.of(PROCESSOR_ORACLE, PROCESSOR_MYSQL, PROCESSOR_DATA_LAKE);


    /**
     * 更新周期
     */
    public static final String DL_RESOURCE_UPDATE_CYCLE = "dl.resource.update.cycle";
    /**
     * 更新周期 —— 实时
     */
    public static final Long RESOURCE_UPDATE_CYCLE_HOUR = 1L;
    /**
     * 更新周期 —— 每日
     */
    public static final Long RESOURCE_UPDATE_CYCLE_DAY = 2L;
    /**
     * 更新周期 —— 每周
     */
    public static final Long RESOURCE_UPDATE_CYCLE_WEEK = 3L;
    /**
     * 更新周期 —— 每月
     */
    public static final Long RESOURCE_UPDATE_CYCLE_MONTH = 4L;
    /**
     * 更新周期 —— 每季度
     */
    public static final Long RESOURCE_UPDATE_CYCLE_QUARTER = 5L;
    /**
     * 更新周期 —— 每半年
     */
    public static final Long RESOURCE_UPDATE_CYCLE_HALF_YEAR = 6L;
    /**
     * 更新周期 —— 每年
     */
    public static final Long RESOURCE_UPDATE_CYCLE_YEAR = 7L;
    /**
     * 更新周期 —— 一次性
     */
    public static final Long RESOURCE_UPDATE_CYCLE_DISPOSABLE = 8L;
    /**
     * 更新周期 —— 不提供
     */
    public static final Long RESOURCE_UPDATE_CYCLE_NOT_PROVIDED = 9L;
    /**
     * 更新周期 —— 其他
     */
    public static final Long RESOURCE_UPDATE_CYCLE_OTHER = 10L;
    /**
     * 更新周期List
     */
    public static final List<Long> RESOURCE_UPDATE_CYCLE_LIST = ImmutableList.of(RESOURCE_UPDATE_CYCLE_HOUR, RESOURCE_UPDATE_CYCLE_DAY, RESOURCE_UPDATE_CYCLE_WEEK, RESOURCE_UPDATE_CYCLE_MONTH, RESOURCE_UPDATE_CYCLE_QUARTER, RESOURCE_UPDATE_CYCLE_HALF_YEAR, RESOURCE_UPDATE_CYCLE_YEAR, RESOURCE_UPDATE_CYCLE_DISPOSABLE, RESOURCE_UPDATE_CYCLE_NOT_PROVIDED, RESOURCE_UPDATE_CYCLE_OTHER);


    /**
     * 资源状态
     */
    public static final String DL_RESOURCE_RESOURCE_STATE = "dl.resource.resource.state";
    /**
     * 资源状态 —— 正常
     */
    public static final Long RESOURCE_STATE_EFFECTIVE = 1L;
    /**
     * 资源状态 —— 未上线
     */
    public static final Long RESOURCE_STATE_INVALID = 2L;
    /**
     * 资源状态 —— 已停更
     */
    public static final Long RESOURCE_STATE_STOP_UPDATE = 3L;
    /**
     * 资源状态 —— 草稿
     */
    public static final Long RESOURCE_STATE_DRAFT = 4L;
    /**
     * 资源状态 —— 已禁用
     */
    public static final Long RESOURCE_STATE_FORBIDDEN = 5L;
    /**
     * 资源状态 —— 待审批
     */
    public static final Long RESOURCE_STATE_WAIT_FOR_APPROVE = 6L;

    /**
     * 资源状态去除“草稿”
     */
    public static final List<Long> RESOURCE_STATE_WITHOUT_DRAFT = ImmutableList.of(RESOURCE_STATE_EFFECTIVE, RESOURCE_STATE_INVALID, RESOURCE_STATE_STOP_UPDATE);
    /**
     * 资源状态去除“无效”和“草稿”
     */
    public static final List<Long> RESOURCE_STATE_WITHOUT_DRAFT_AND_INVALID = ImmutableList.of(RESOURCE_STATE_EFFECTIVE, RESOURCE_STATE_STOP_UPDATE);

    /**
     * 资源状态（新增可选的）（未上线、草稿）
     */
    public static final List<Long> RESOURCE_STATE_ALLOW_FOR_SAVE_NEW = ImmutableList.of(RESOURCE_STATE_INVALID, RESOURCE_STATE_DRAFT);
    /**
     * 资源状态（可修改的）（未上线、草稿、已禁用）
     */
    public static final List<Long> RESOURCE_STATE_ALLOW_MODIFY = ImmutableList.of(RESOURCE_STATE_INVALID, RESOURCE_STATE_DRAFT, RESOURCE_STATE_FORBIDDEN);


    /**
     * 资源安全级别
     */
    public static final String DL_RESOURCE_SECURITY_LEVEL = "dl.resource.security.level";
    /**
     * 资源安全级别 —— 未分级
     */
    public static final Long SECURITY_LEVEL_UNGRADED = 1L;
    /**
     * 资源安全级别—— 内部
     */
    public static final Long SECURITY_LEVEL_INTERNAL = 2L;
    /**
     * 资源安全级别—— 秘密
     */
    public static final Long SECURITY_LEVEL_SECRET = 3L;
    /**
     * 资源安全级别 —— 机密
     */
    public static final Long SECURITY_LEVEL_CONFIDENTIAL = 4L;
    /**
     * 资源安全级别 —— 绝密
     */
    public static final Long SECURITY_LEVEL_TOP_SECRET = 5L;
    /**
     * 资源安全级别List
     */
    public static final List<Long> SECURITY_LEVEL_LIST = ImmutableList.of(SECURITY_LEVEL_UNGRADED, SECURITY_LEVEL_INTERNAL, SECURITY_LEVEL_SECRET, SECURITY_LEVEL_CONFIDENTIAL, SECURITY_LEVEL_TOP_SECRET);

    /**
     * 变更状态
     */
    public static final String DL_RESOURCE_CHANGE_STATE = "dl.resource.change.state";
    /**
     * 变更状态 —— 发版
     */
    public static final Long CHANGE_STATE_RELEASE = 1L;
    /**
     * 变更状态 —— 启用
     */
    public static final Long CHANGE_STATE_ENABLE = 2L;
    /**
     * 变更状态 —— 停用
     */
    public static final Long CHANGE_STATE_FORBIDDEN = 3L;
    /**
     * 变更状态 —— 停更
     */
    public static final Long CHANGE_STATE_STOP_UPDATE = 4L;
    /**
     * 变更状态 —— 导出
     */
    public static final Long CHANGE_STATE_EXPORT = 5L;


    /**
     * 资源分类
     */
    public static final String DL_RESOURCE_TAG_TYPE = "dl.resource.tag.type";
    /**
     * 资源分类（标签类别） —— 主题分类
     */
    public static final Long RESOURCE_LABEL_THEME = 1L;
    /**
     * 资源分类（标签类别） —— 行业分类
     */
    public static final Long RESOURCE_LABEL_INDUSTRY = 2L;
    /**
     * 资源分类（标签类别） ——  服务分类
     */
    public static final Long RESOURCE_LABEL_SERVICE = 3L;
    /**
     * 资源分类（标签类别） —— 数据领域
     */
    public static final Long RESOURCE_LABEL_DATA_DOMAIN = 4L;
    /**
     * 资源分类（标签类别） —— 资源形态分类
     */
    public static final Long RESOURCE_LABEL_RESOURCE_FORM = 5L;


    /**
     * 数据领域（国标）
     */
    public static final String DL_RESOURCE_DATA_DOMAIN = "dl.resource.data.domain";

    /**
     * 数据库类型 ——  HIVE
     */
    public static final Long DATABASE_TYPE_HIVE = 1L;
    /**
     * 数据库类型 —— MPP
     */
    public static final Long DATABASE_TYPE_MPP = 2L;
    /**
     * 数据库类型 —— Oracle （子湖、汇聚平台有使用）
     */
    public static final Long DATABASE_TYPE_ORACLE = 3L;
    /**
     * 数据库类型 —— MYSQL （汇聚平台有用）
     */
    public static final Long DATABASE_TYPE_MYSQL = 4L;
    /**
     * 数据库类型List
     */
    public static final List<Long> DATABASE_TYPE_LIST = ImmutableList.of(DATABASE_TYPE_HIVE, DATABASE_TYPE_MPP);


    /**
     * 数据质量
     */
    public static final String DL_RESOURCE_DATA_QUALITY = "dl.resource.data.quality";
    /**
     * 数据质量 —— 优
     */
    public static final Long RESOURCE_DATA_QUALITY_EXCELLENT = 1L;
    /**
     * 数据质量 —— 良
     */
    public static final Long RESOURCE_DATA_QUALITY_FINE = 2L;
    /**
     * 数据质量 —— 差
     */
    public static final Long RESOURCE_DATA_QUALITY_BAD = 3L;


    /**
     * 字段公开属性
     */
    public static final String COLUMN_OPEN_SCOPE = "dl.column.open.scope";
    /**
     * 字段公开属性 —— 对外公开
     */
    public static final Long COLUMN_OPEN_SCOPE_PUBLIC = 1L;
    /**
     * 字段公开属性 —— 对内公开
     */
    public static final Long COLUMN_OPEN_SCOPE_INTERNAL = 2L;
    /**
     * 字段公开属性 —— 不公开
     */
    public static final Long COLUMN_OPEN_SCOPE_PRIVATE = 3L;
    /**
     * 字段公开属性 List
     */
    public static final List<Long> COLUMN_OPEN_SCOPE_LIST = ImmutableList.of(COLUMN_OPEN_SCOPE_PUBLIC, COLUMN_OPEN_SCOPE_INTERNAL, COLUMN_OPEN_SCOPE_PRIVATE);


    /**
     * 字段数据类型
     */
    public static final String COLUMN_DATATYPE = "dl.column.datatype";
    /**
     * 字段数据类型 —— 短字符
     */
    public static final Long COLUMN_DATATYPE_SHORT_STRING = 1L;
    /**
     * 字段数据类型 —— 较长字符
     */
    public static final Long COLUMN_DATATYPE_MIDDLE_STRING = 2L;
    /**
     * 字段数据类型 —— 长字符
     */
    public static final Long COLUMN_DATATYPE_LONG_STRING = 3L;
    /**
     * 字段数据类型 —— 日期型
     */
    public static final Long COLUMN_DATATYPE_DATE = 4L;
    /**
     * 字段数据类型 —— 整数型
     */
    public static final Long COLUMN_DATATYPE_INTEGER = 5L;
    /**
     * 字段数据类型 —— 小数型
     */
    public static final Long COLUMN_DATATYPE_DECIMAL = 6L;
    /**
     * 字段数据类型 LIST
     */
    public static final List<Long> COLUMN_DATATYPE_LIST = ImmutableList.of(COLUMN_DATATYPE_SHORT_STRING, COLUMN_DATATYPE_MIDDLE_STRING, COLUMN_DATATYPE_LONG_STRING, COLUMN_DATATYPE_DATE, COLUMN_DATATYPE_INTEGER, COLUMN_DATATYPE_DECIMAL);


    /**
     * 新增
     */
    public static final Long OPERATION_TYPE_ADD = 1L;
    /**
     * 删除
     */
    public static final Long OPERATION_TYPE_DELETE = 2L;
    /**
     * 修改
     */
    public static final Long OPERATION_TYPE_UPDATE = 3L;


    /**
     * 信息项模板 业务类型
     */
    public static final String COLUMN_TEMPLATE_BUSINESS_TYPE = "columnTemplate";

    /**
     * 信息项模板 业务ID
     */
    public static final String COLUMN_TEMPLATE_BUSINESS_ID = "1";


    /**
     * 新旧版本信息项List 自定义查询交集信息项定义（信息项名称相同且数据类型相同）
     */
    public static Map<String, List<String>> CUSTOM_COLUMN_INTERSECTION_MAP = new HashMap<>(16);
    /**
     * 旧版本信息项定义 key
     */
    public static final String LAST_COLUMN_DEFINITION_INTERSECTION = "lastColumnDefinitionIntersection";
    /**
     * 新版本信息项定义 key
     */
    public static final String NEW_COLUMN_DEFINITION_INTERSECTION = "newColumnDefinitionIntersection";


    /**
     * 信息项历史缓存（每个用户的最大条数）
     */
    public static final int COLUMN_HISTORY_MAX_SIZE_PER_USER = 50;

    /**
     * 目录类型 业务类型
     */
    public static final String CATALOG_TYPE_BUSINESS_TYPE = "catalogType";

    /**
     * 资源目录数据引用 —— 非引用
     */
    public static final Long RESOURCE_QUOTE_NO = 0L;

    /**
     * 资源目录数据引用 —— 引用
     */
    public static final Long RESOURCE_QUOTE_YES = 1L;

    /**
     * 资源目录共享属性 —— 有条件共享
     */
    public static final Long SHARED_PROPERTY_SOME = 1L;

    /**
     * 资源目录共享属性 —— 无条件共享
     */
    public static final Long SHARED_PROPERTY_ALL = 2L;

    /**
     * 资源目录共享属性 —— 不予共享
     */
    public static final Long SHARED_PROPERTY_No = 3L;

    public static final List<Long> SHARED_PROPERTY_LIST = ImmutableList.of(SHARED_PROPERTY_SOME, SHARED_PROPERTY_ALL, SHARED_PROPERTY_No);

}
