package com.openjava.datalake.rescata.service;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.rescata.domain.DataCode;
import com.openjava.datalake.rescata.repository.DataCodeRepository;
import com.openjava.datalake.rescata.vo.DataCodeVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author JiaHai
 * @Description 数据字典业务层接口实现类
 */
@Service
public class DataCodeServiceImpl implements DataCodeService {
    @Autowired
    private DataCodeRepository dataCodeRepository;

    @Override
    public List<DataCode> findByCodetype(String codetype) {
        return dataCodeRepository.findByCodetype(codetype);
    }

    @Override
    public List<DataCodeVo> findVoByCodetype(String codetype) {
        List<DataCode> dataCodeList = this.findByCodetype(codetype);
        if (CollectionUtils.isNotEmpty(dataCodeList)) {
            List<DataCodeVo> dataCodeVoList = new ArrayList<>();
            dataCodeList.stream().forEach(dataCode ->
                    dataCodeVoList.add(new DataCodeVo(Long.valueOf(dataCode.getCode()), dataCode.getCodetype(), dataCode.getCodename(), dataCode.getCodevalue()))
            );
            return dataCodeVoList;
        }
        return null;
    }

    @Override
    public Map<String, String> findByCodetypeToMap(String codetype) {
        List<DataCode> byCodetype = this.findByCodetype(codetype);
        Map<String, String> dlresourceresourcestate = byCodetype.stream().collect(Collectors.toMap(DataCode::getCode, DataCode::getCodename));
        return dlresourceresourcestate;
    }

    @Override
    public String findCodeNameByCodetypeAndCodeFromCache(String codetype, Long code) {
        if (null == code) {
            code = -9999L;
        }
        // 从缓存中读取
        return PublicConstant.dataDictionaryCacheMap.get(codetype).get(code.toString());
    }

    @Override
    public Map<String, DataCode> getCodeMap(String s) {
        List<DataCode> byCodetype = this.findByCodetype(s);
        Map<String, DataCode> collect = byCodetype.stream().collect(Collectors.toMap(DataCode::getCode, Function.identity()));
        return collect;
    }

    @Override
    public List<DataCode> findByCodetypeLike(String codetype) {
        if (StringUtils.isNotBlank(codetype)) {
            // 前缀 模糊匹配
            codetype += "%";

            // 查询，并返回查询结果
            return dataCodeRepository.findByCodetypeLike(codetype);
        }
        return null;
    }

    @Override
    @PostConstruct
    public void initDataDictionaryOfDataLake() {
        // 查询 数据湖 数据字典
        List<DataCode> dataCodeList = this.findByCodetypeLike(PublicConstant.DATA_LAKE_DATA_DICTIONARY_PREFIX);
        if (CollectionUtils.isNotEmpty(dataCodeList)) {
            // 数据字典 不为空
            Map<String, List<DataCode>> dataCodeListMap = dataCodeList.stream().collect(Collectors.groupingBy(DataCode::getCodetype));
            for (String key : dataCodeListMap.keySet()) {
                Map<String, String> map = dataCodeListMap.get(key).stream().collect(Collectors.toMap(DataCode::getCode, DataCode::getCodename));
                // 添加进缓存中
                PublicConstant.dataDictionaryCacheMap.put(key, map);
            }

            // 添加额外数据字典 —— 系统通用
            Map<String, String> map = new HashMap<>(16);
            map.put(PublicConstant.YES.toString(), "是");
            map.put(PublicConstant.NO.toString(), "否");
            PublicConstant.dataDictionaryCacheMap.put(PublicConstant.PUBLIC_YN, map);
        }
    }

    @Override
    public void refreshDataDictionaryOfDataLake() {
        // 清空 数据湖 数据字典缓存
        PublicConstant.dataDictionaryCacheMap.clear();
        // 初始化 数据湖 数据字典缓存
        this.initDataDictionaryOfDataLake();
    }
}
