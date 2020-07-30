package com.openjava.datalake.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


/**
 * ioc 配置信息
 *
 * @author luyaoquan
 */
@Component
@Data
@ConfigurationProperties(prefix = "iocplatform.url")
public class IocPlatformConfig {

    private String iocAdmin;
    private String adminDepartGetChild;
    private String adminDepartGet;
    private String adminDepartTopDept;
    private String adminDepartGetTree;
    private String adminUserLogin;
    private String userAccount;
    private String userPwd;
    private String adminDepartRootId;
    private String adminDepartSearch;
    private String orgtypeGetDepart;
    private String adminDepartZtree;
    private String orgRoleGetUsers;

}
