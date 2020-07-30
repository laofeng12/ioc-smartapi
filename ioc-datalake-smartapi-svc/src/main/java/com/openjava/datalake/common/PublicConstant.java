package com.openjava.datalake.common;

import com.google.common.collect.ImmutableList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author ALL
 * @Description 数据字典
 */
public class PublicConstant {
    public static String ACTIVE;  //当前环境
    private static String PROPERTY_NAME = "application.yml";//主配置文件
    public static String HUAWEI_LOCAL_RESOURCE_PATH = "";//华为本地资源路径
    public static String HUAWEI_PROD_RESOURCE_PATH = "";//华为openshif上的资源路径

    public static void initHuaweiEnv() {
        System.out.println(PublicConstant.ACTIVE);
        System.out.println("govtest".equals(PublicConstant.ACTIVE));
        if ("govprod".equals(PublicConstant.ACTIVE)) {
            HUAWEI_LOCAL_RESOURCE_PATH = "/huawei/prod/";
            HUAWEI_PROD_RESOURCE_PATH = "/datalake/huawei/kafka/prod/";
        } else if ("govtest".equals(PublicConstant.ACTIVE)) {
            HUAWEI_LOCAL_RESOURCE_PATH = "/huawei/test/";
            HUAWEI_PROD_RESOURCE_PATH = "/datalake/huawei/kafka/test/";
        } else {
            HUAWEI_LOCAL_RESOURCE_PATH = "/huawei/test/";
            HUAWEI_PROD_RESOURCE_PATH = HUAWEI_LOCAL_RESOURCE_PATH;
        }
        System.out.println("华为资源路径test::" + HUAWEI_LOCAL_RESOURCE_PATH);
        System.out.println("华为资源路径prod:" + HUAWEI_PROD_RESOURCE_PATH);
    }

    /**
     * 数据湖 数据字典 前缀
     */
    public static final String DATA_LAKE_DATA_DICTIONARY_PREFIX = "dl.";

    /**
     * 数据湖 数据字典缓存Map
     */
    public static Map<String, Map<String, String>> dataDictionaryCacheMap = new HashMap();

    /**
     * 通用的 是否
     */
    public static final String PUBLIC_YN = "public.YN";
    /**
     * 是
     */
    public static final Long YES = 1L;
    /**
     * 否
     */
    public static final Long NO = 0L;


    /**
     * 通用异常
     */
    public static final int EXCEPTION_ERROR = 500;

    public static final Long ROOT_ID = -1L;

    public static final Long INSENSITIVES_RULE_CHAR_SET = 1L;
    public static final Long INSENSITIVES_RULE_STORAGE = 2L;
    public static final String INSENSITIVES_RULE_STORAGE_FRONT = "左";
    public static final String INSENSITIVES_RULE_STORAGE_MIDDLE = "中";
    public static final String INSENSITIVES_RULE_STORAGE_LATER = "右";

    // 资源目录对外公开属性
    public static final Long RESOURCE_OPEN_SCOPE_ALL_OPEN = 1L;
    public static final Long RESOURCE_OPEN_SCOPE_ALL_INNER = 2L;
    public static final Long RESOURCE_OPEN_SCOPE_PART_INNER = 3L;
    public static final Long RESOURCE_OPEN_SCOPE_NOT_OPEN = 4L;

    // 字段对外公开属性
    public static final Long COLUMN_OPEN_SCOPE_ALL_OPEN = 1L;
    public static final Long COLUMN_OPEN_SCOPE_ALL_INNER = 2L;
    public static final Long COLUMN_OPEN_SCOPE_NOT_OPEN = 3L;

    // 审批状态
    public static final Long SUBSCRIBE_APPROVAL_STATE_UNAPPLY = 0L; // 未申请
    public static final Long SUBSCRIBE_APPROVAL_STATE_AUDITED = 1L; // 已审批
    public static final Long SUBSCRIBE_APPROVAL_STATE_UNCHECK = 2L; // 待审批
    public static final Long SUBSCRIBE_APPROVAL_STATE_DISACCEPT = 3L; // 审批不通过
    public static final Long SUBSCRIBE_APPROVAL_STATE_DOING_AUDIT = 4L; // 审批处理中
    // 信息项审批状态
    public static final Long SUBSCRIBE_STRUCTURE_APPROVAL_STATE_AUDITED = 1L;  // 通过申请
    public static final Long SUBSCRIBE_STRUCTURE_APPROVAL_STATE_UNCHECK = 2L;  // 待审批
    public static final Long SUBSCRIBE_STRUCTURE_APPROVAL_STATE_DISACCEPT = 3L; // 审批不通过

    // 申请不脱敏
    public static final Long SUBSCRIBE_APPLY_SENSITIVES = 2L;
    // 申请不加密
    public static final Long SUBSCRIBE_APPLY_DECRYPTION = 2L;
    // 权限审批情况
    public static final Long PERMISSION_APPROVE_STATE_DISACCEPT = 0L;
    public static final Long PERMISSION_APPROVE_STATE_ACCEPT = 1L;
    public static final Long PERMISSION_APPROVE_STATE_UNCHECK = 2L;

    // 需求状态
    public static final Long DEMAND_STATE_DRAFT = 1L; // 草稿
    public static final Long DEMAND_STATE_APPLIED = 2L; // 已发起
    public static final Long DEMAND_STATE_WITHDRAW = 3L; // 已撤回
    public static final Long DEMAND_STATE_REPLIED = 4L; // 已回复

    // 资源目录权限级别
    public static final Long RESOURCE_PERMISSION_LEVEL_ALL = 1L; // 全部权限（不用订阅）
    public static final Long RESOURCE_PERMISSION_LEVEL_PART = 2L; // 部分权限（要订阅）
    public static final Long RESOURCE_PERMISSION_LEVEL_NONE = 3L; // 没有权限（要订阅）

    // 拥有字段的全部权限的情况
    public static final Long RESOURCE_FULLPERMIT_LEVEL_NONE = 0L; // 0一个拥有全部权限的字段都没有
    public static final Long RESOURCE_FULLPERMIT_LEVEL_ANY = 1L; // 1至少拥有一个字段有全部权限
    public static final Long RESOURCE_FULLPERMIT_LEVEL_ALL = 2L; // 2全部字段都有字段的全部权限

    // API推送 接口类型
    public static final Long API_PUSH_TYPE_PUT = 0L;
    public static final Long API_PUSH_TYPE_PATCH = 1L;
    public static final Long API_PUSH_TYPE_DELETE = 2L;

    // API（接口）状态
    public static final Long API_ENABLE_STATE = 0L; // 启用
    public static final Long API_DISABLE_STATE = 1L; // 禁用

    // 资源目录信息项类别
    public static final String COLUMN_TYPE_NUMBER = "number"; // 值类型(整数、小数)
    public static final String COLUMN_TYPE_STRING = "string"; // 字符类型(短字符、较长字符、长字符)
    public static final String COLUMN_TYPE_DATE = "date"; // 时间类型

    // 数据提供状态(dl.data.provide.state)0未提交，1已提交
    public static final Long DATA_PROVIDE_STATE_UNPROV = 0L;
    public static final Long DATA_PROVIDE_STATE_PROVED = 1L;

    // 数据提供管理-同步任务状态（dl.data.provide.sync.state）0同步中，1同步成功，2同步失败
    public static final Long DATA_PROVIDE_STATE_SYNCING = 0L;
    public static final Long DATA_PROVIDE_STATE_SYNC_SUCCESS = 1L;
    public static final Long DATA_PROVIDE_STATE_SYNC_FAILED = 2L;

    /**
     * 归集库 数据库建表的隐藏字段
     */
    public static final List<String> COLUMN_SUPPLY_FOR_HJ = ImmutableList.of("id_tokimnhj", "dir_id_tokimnhj", "addtime_tokimnhj", "updatetime_tokimnhj", "state_tokimnhj", "hash_tokimnhj", "data_hash_tokimnhj");

    // 关联机构的关系类型（1下级机构、2连锁机构）
    /**
     * 机构关系 —— 下级机构
     */
    public static final Long SUBORDINATE_ORGANIZATION_RELATION = 1L;
    /**
     * 机构关系 —— 连锁机构
     */
    public static final Long CHAIN_ORGANIZATION_RELATION = 2L;

    // 质量报告工单
    // 发送状态(dl.quality.send.state)（1已发起，2未发起）
    public static final Long QUALITY_SEND_STATE_SENDEN = 1L;
    public static final Long QUALITY_SEND_STATE_UNSEND = 2L;

    // 工单处理情况(dl.qualty.deal.type)（1已处理；2误报；3无法处理）
    public static final Long QUALITY_DEAL_TYPE_DONE = 1L;
    public static final Long QUALITY_DEAL_TYPE_ERROR_REPORT = 2L;
    public static final Long QUALITY_DEAL_TYPE_UNABLE = 3L;

    // 处理状态（总状态）(dl.quality.deal.state)（1处理中；2已处理；3已办结）
    public static final Long QUALITY_DEAL_STATE_DOING = 1L;
    public static final Long QUALITY_DEAL_STATE_DONE = 2L;
    public static final Long QUALITY_DEAL_STATE_FINISH = 3L;

    // 审批状态(dl.quality.approval.state)（1待审批；2通过；3不通过）
    public static final Long QUALITY_APPORAL_STATE_UNDO = 1L;
    public static final Long QUALITY_APPORAL_STATE_ACCEPT = 2L;
    public static final Long QUALITY_APPORAL_STATE_REJECT = 3L;

    // 类型(dl.quality.track.type)（ 1工单处理信息；2工单审批信息）
    public static final Long QUALITY_TRACK_TYPE_DEAL = 1L;
    public static final Long QUALITY_TRACK_TYPE_APPROVAL = 2L;

    /**
     * 数据库类型（DTS）
     */
    public static final String DTS_DATABASE_TYPE = "dts.database.type";

    /**
     * IOC政务大脑数据实时交换接口版本号
     * 最新版本号
     */
    public static final Long DL_SYNC_API_NEWEST_V = 1L;//最新版本号
    public static final Long DL_SYNC_API_V0 = 0L;//初始版本号

    /**
     * EXCEL批处理数据任务类型
     */
    public static final String EXCEL_PROCESS_TYPE_DELETE = "EXCEL_PROCESS_DELETE";
    public static final String EXCEL_PROCESS_TYPE_ONLY_INSERT = "EXCEL_PROCESS_ONLY_INSERT";
    public static final String EXCEL_PROCESS_TYPE_UPDATE_OR_INSERT = "EXCEL_PROCESS_UPDATE_OR_INSERT";
    public static final String EXCEL_PROCESS_TYPE_DELETEALL_AND_INSERT = "EXCEL_PROCESS_DELETEALL_AND_INSERT";
    // excel处理任务历史状态
    public static final Long EXCEL_PROCESS_STATE_ALL_SUCCESS = 1L;
    public static final Long EXCEL_PROCESS_STATE_ALL_FAIL = 2L;
    public static final Long EXCEL_PROCESS_STATE_PART_FAIL = 3L;

    //需求协作工单-需求状态(dl.require.order.state)
    /**
     * 进行中
     */
    public static final Long DL_REQUIRE_WORK_ORDER_ONGOING = 1L;

    //需求协作工单-需求类型(dl.require.order.type)
    /**
     * 数据需求
     */
    public static final Long DL_REQUIRE_WORK_ORDER_DATA_REQUIRE = 1L;

    /**
     * 完成进度排名展示个数
     */
    public static final Integer DL_REQUIRE_COMPLETED_EFFECT_RANK_NUM = 5;

    /*
    数据挂载任务 数据状态
    数据提供状态（dl.data.provide.state）0同步中、1同步成功、2同步失败、3未提交、4已提交
     */
    public static final Long RES_DATA_PROVIDE_STATE_SYNCING = 0L;
    public static final Long RES_DATA_PROVIDE_STATE_SYNC_SUCC = 1L;
    public static final Long RES_DATA_PROVIDE_STATE_SYNC_FAIL = 2L;
    // 未提交
    public static final Long RES_DATA_PROVIDE_STATE_UNSUBMIT = 3L;
    // 已提交
    public static final Long RES_DATA_PROVIDE_STATE_SUBMITED = 4L;

    /**
     * 角色 —— 系统管理员
     */
    public static final String SYSTEM_ADMIN = "SYSAdmin";
    /**
     * 角色 —— 测试运维人员
     */
    public static final String MAINTAINER = "Maintainer";

    /**
     * 角色 —— 管理员
     */
    public static final String ADMIN = "Admin";
    /**
     * 角色 —— 审核员
     */
    public static final String AUDITOR = "Auditor";
    /**
     * 角色 —— 目录管理员
     */
    public static final String RESOURCE_ADMIN = "ResourceAdmin";
    /**
     * 角色 —— 科室管理员
     */
    public static final String DEPT_ADMIN = "DeptResourceAdmin";
    /**
     * 角色 —— 使用人员
     */
    public static final String USER = "User";
    /**
     * 角色 —— 委办局管理员
     */
//    public static final String WBJ_ADMIN = "WBJAdmin";

    /**
     * 角色 —— 委办局审核员
     */
//    public static final String WBJ_AUDITOR = "WBJAuditor";

    /**
     * 角色 —— 数据提供人
     */
    public static final String DATA_PROVIDER = "DataProvider";

    /**
     * 政数局部门code
     */
    public static final String ZSJ_DEPT_CODE = "DGS060";
    /**
     * 政数局组织机构ID
     */
    public static final String ZSJ_ORG_ID = "4028818e41fb0cad0141fb0e92af000d";
    /**
     * 操作行为 —— 删除数据
     */
    public static final String ACTION_DELETE_DATA = "【删除数据】 ";
    /**
     * 操作行为 —— 更新数据
     */
    public static final String ACTION_UPDATE_DATA = "【更新数据】 ";


    /**
     * 分布式事务相关
     */
    public static final Long DL_RETRY_RULD_ID = 1L;//重试规制id
    public static final Long DL_EVENT_PUBLISH_NO = 0L;//未发布
    public static final Long DL_EVENT_PUBLISH_YES = 1L;//已发布
    public static final Long DL_EVENT_PUBLISH_API = 1L;//api接口入库
    public static final Long DL_EVENT_PROCESS_NO = 0L;//未发布
    public static final Long DL_EVENT_PROCESS_YES = 1L;//已发布
    public static final Long DL_EVENT_PROCESS_API = 1L;//监听推送数据
    public static final Long DL_EVENT_PROCESS_LOG_ERROR = 0L;//推送失败
    public static final Long DL_EVENT_PROCESS_LOG_NO_BEGIN = 1L;//未推送
    public static final Long DL_EVENT_PROCESS_LOG_SUCCESS = 2L;//推送成功

    /**
     * 签名方式
     */
    public static final String DL_HMAC_SHA256 = "HMAC-SHA256";
    public static final String DL_SM3 = "SM3";
    public static final String DL_SM4 = "SM4";

    /**
     * 更新的数据方式
     */
    public static final String DL_INSERT = "insert";
    public static final String DL_UPDATE = "update";
    public static final String DL_DELETE = "delete";

    /**
     * tpoic
     */
    public static   final String DL_TOPIC1 = "datalake-topic1";
    public static   final String DL_TOPIC2 = "datalake-topic2";
    public static   final String DL_TOPIC3 = "datalake-topic3";

    /**
     * jvm缓存区名称
     */
    public static String GAT_WAY_USERINFO_DATA = "gateway-userInfo-Data";

    /**

    /**
     * 系统：0:数据管理平台,1:数据湖,
     */
    public static   final Integer DATALAKE_SYSTEM=1;

//    public static Boolean IsAdmin() {
//        // 获取当前登录者信息
//        OaUserVO oaUserVO = (OaUserVO) SsoContext.getUser();
//        // 获取当前登录者的角色列表
//        List<String> roleAlias = oaUserVO.getRoleAlias();
//        // 权限校验 （非 系统管理员 || 政数局管理员 || 委办局管理员 || 目录管理员； 只有中心库）
//        if (CollectionUtils.isNotEmpty(roleAlias)&&(roleAlias.contains(SYSTEM_ADMIN) || roleAlias.contains(ADMIN) || roleAlias.contains(RESOURCE_ADMIN))) {
//            return true;
//        }
//        return false;
//    }
    
    /**
     * 文件类型
     */
    public static final String DL_RESOURCE_FILETYPE = "dl.resource.filetype";
    /**
     * 文本
     */
    public static final Long TXT = 1L;
    /**
     * 图片
     */
    public static final Long PICTURE = 2L;
    /**
     * 视频
     */
    public static final Long VIDEO = 3L;
    /**
     * 音频
     */
    public static final Long AUDIO = 4L;
    
    
    /**
     * 标签分类类型
     */
    public static final String DL_RESOURCE_TAGE_TYPE = "dl.resource.tage_type";
    /**
     * 国标标签
     */
    public static final Long GB = 1L;
    /**
     * 数字政府领域标签
     */
    public static final Long DIG_GOV = 2L;
    /**
     * 自定义标签
     */
    public static final Long CUSTOM = 3L;
    
    
    /**
     * 挂载方式（数据提供方式）
     */
    public static final String DL_RESOURCE_DATA_PROVIDE_MODE = "dl.resource.data.provide.mode";
    /**
     * 库表挂载
     */
    public static final Long KBGZ = 1L;
    /**
     * 附件上传
     */
    public static final Long FJSC = 2L;
    /**
     * 接口获取
     */
    public static final Long JKHQ = 3L;
    /**
     * 数据归集
     */
    public static final Long SJGJ = 4L;
    /**
     * 第三方接口
     */
    public static final Long DSFJK = 5L;
    
}
