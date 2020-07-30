package com.openjava.datalake.push.vo;

import com.openjava.datalake.dataxjdbcutil.model.TableContext;
import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("Kafka接口同步消息实体类")
public class KafkaApiSyncMessageVO {

    private Long resourceId;//资源id

    private String resourceCode;//资源编码

    private String version;//资源版本号

    private TableContext tableContext;//需要同步数据

    private String recordSequence;//本次同步记录的唯一序列标识

    private Long userId;//用户id

    private  ApiSyncReceiveDataVO syncReceiveDataVO;//同步的数据

}
