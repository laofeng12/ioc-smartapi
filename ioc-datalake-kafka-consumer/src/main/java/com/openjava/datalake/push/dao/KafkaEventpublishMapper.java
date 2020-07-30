package com.openjava.datalake.push.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openjava.datalake.push.domain.KafkaEventpublish;

/**
 * <p>
 * 事件发布表 Mapper 接口
 * 支持CRUD操作
 * </p>
 *
 * @author zmk
 * @since 2020-02-28
 */
public interface KafkaEventpublishMapper extends BaseMapper<KafkaEventpublish> {

//    @Select("select * from KAFKA_EVENTPUBLISH where 1=1 ${ew.customSqlSegment} and ROWNUM <= 10")
//    List<KafkaEventpublish> getTenData(@Param(Constants.WRAPPER) QueryWrapper<KafkaEventpublish> wrapper);

}
