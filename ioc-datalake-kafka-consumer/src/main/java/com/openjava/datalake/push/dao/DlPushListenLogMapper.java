package com.openjava.datalake.push.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.openjava.datalake.push.domain.DlPushListenLog;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 监听事件日志记录 Mapper 接口
 * 支持CRUD操作
 * </p>
 *
 * @author zmk
 * @since 2020-02-28
 */
public interface DlPushListenLogMapper extends BaseMapper<DlPushListenLog> {
    @Select("select * from DL_PUSH_LISTEN_LOG t where t.STATES=1 for update")
    List<DlPushListenLog> getNobegin();
//    List<DlPushListenLog> get(@Param(Constants.WRAPPER) QueryWrapper<DlPushListenLog> wrapper);

}
