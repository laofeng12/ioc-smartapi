package com.openjava.datalake.rescata.service;

import com.openjava.datalake.rescata.domain.DlResourceDatasource;
import com.openjava.datalake.rescata.repository.DlResourceDatasourceRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录关联数据源（汇聚） 业务层接口实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DlResourceDatasourceServiceImpl implements DlResourceDatasourceService {
    @Autowired
    private DlResourceDatasourceRepository dlResourceDatasourceRepository;

    @Override
    public DlResourceDatasource findByResourceCode(String resourceCode) {
        List<DlResourceDatasource> dlResourceDatasourceList = dlResourceDatasourceRepository.findByResourceCode(resourceCode);
        if (CollectionUtils.isNotEmpty(dlResourceDatasourceList)) {
            return dlResourceDatasourceList.get(0);
        }
        return null;
    }

}
