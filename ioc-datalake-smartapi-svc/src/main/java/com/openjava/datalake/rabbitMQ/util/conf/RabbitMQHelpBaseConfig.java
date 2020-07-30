package com.openjava.datalake.rabbitMQ.util.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description： RabbitMQ组件的使用帮助类配置信息
 * @Author： zjf
 * @Date: 2020-4-22 11:20
 */
@Component
@ConfigurationProperties(prefix = "rabbitmqhelp.base")
@Data
public class RabbitMQHelpBaseConfig {
    /**
     * mq是否注册
     */
    private Boolean rabbitmqSignIn;

    /**
     * 当前服务是否是管理端
     */
    private Boolean currentServiceIsAdmin;

    /**
     * 当前服务名称的缩写
     */
    private String currentServiceShortName;

    /**
     * 管理端的服务名称的缩写
     */
    private String adminServiceShortName;
}
