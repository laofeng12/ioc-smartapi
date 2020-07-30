package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.PublicConstant;
import org.springframework.stereotype.Service;


/**
 * @Author JiaHai
 * @Description 数据字典业务层接口实现类
 */
@Service
public class DataCodeServiceImpl implements DataCodeService {

    @Override
    public String findCodeNameByCodetypeAndCodeFromCache(String codetype, Long code) {
        if (null == code) {
            code = -9999L;
        }
        // 从缓存中读取
        return PublicConstant.dataDictionaryCacheMap.get(codetype).get(code.toString());
    }

}
