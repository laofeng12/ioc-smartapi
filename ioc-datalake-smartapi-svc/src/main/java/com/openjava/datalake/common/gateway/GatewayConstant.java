package com.openjava.datalake.common.gateway;

/**
 * @Author xjd
 * @Date 2019/10/22 15:16
 * @Version 1.0
 */
public class GatewayConstant {

    // API类型
    // 提交接口
    public static final Long MODULE_TYPE_PUSH = 9L;
    // 查询接口
    public static final Long MODULE_TYPE_QUERY = 1L;

    // API 共享的范围
    // 共享到全市
    public static final Long PUBLISH_SHARE_SCOPE_CITY = 1L;
    // 共享到部门
    public static final Long PUBLISH_SHARE_SCOPE_TOP_DEPT = 2L;

    // API共享到市场的公开范围
    // 不公开
    public static final Long PUBLISH_OPEN_RANGE_PRIVATE = 1L;
    // 公开
    public static final Long PUBLISH_OPEN_RANGE_PUBLIC = 2L;


}
