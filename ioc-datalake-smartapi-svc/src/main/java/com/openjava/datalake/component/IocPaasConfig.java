package com.openjava.datalake.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author xjd
 * @Date 2019/9/9 15:47
 * @Version 1.0
 */
@Component
@Data
@ConfigurationProperties(prefix="iocpaas.url")
public class IocPaasConfig {

    private String iocPaas;
    // 网关平台-注册
    private String iocpaasSyncCustomApi;
    // 网关平台-获取API凭证
    private String iocpaasCustomApiCredential;
    // 根据token获取用户信息
    private String iocpaasDecodeToken;
    // 获取网关代理地址
    private String gatewayIpport;
    //同步删除API
    private String syncDeleteApi;
    //网关平台-根据资源目录编码获取API凭证
    private String iocpassSpDlResJwt;
    // 同步更新第三方API的局与科室信息
    private String gatewaySyncCustomApiDept;
    // api共享到市场
    private String iocpassApiPublish;
    // api从市场下架
    private String iocpassApiUnpublish;

}
