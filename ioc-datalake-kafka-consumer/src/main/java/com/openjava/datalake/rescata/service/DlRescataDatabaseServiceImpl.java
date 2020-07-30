package com.openjava.datalake.rescata.service;

import com.alibaba.fastjson.JSON;
import com.openjava.datalake.rescata.domain.DlRescataDatabase;
import com.openjava.datalake.rescata.repository.DlRescataDatabaseRepository;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import org.apache.commons.lang3.StringUtils;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Author JiaHai
 * @Description 数据库连接参数信息 业务层接口实现类
 */
@Service
public class DlRescataDatabaseServiceImpl implements DlRescataDatabaseService {
    @Autowired
    private DlRescataDatabaseRepository dlRescataDatabaseRepository;

    @Autowired
    private BasicTextEncryptor basicTextEncryptor;

    @Override
    public DlRescataDatabase findByRepositoryTypeAndDatabaseType(Long repositoryType, Long databaseType) throws APIException {
        DlRescataDatabase dlRescataDatabase = dlRescataDatabaseRepository.findByRepositoryTypeAndDatabaseType(repositoryType, databaseType);
        if (null == dlRescataDatabase) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "数据库连接信息不存在");
        }
        return dlRescataDatabase;
    }

    @Override
    public List<DlRescataDatabase> findByResourceCode(String resourceCode) {
        return dlRescataDatabaseRepository.findByResourceCode(resourceCode);
    }

    @Override
    public DatabaseInfoVo findByResourceCodeAndRepositoryType(String resourceCode, Long databaseType) throws APIException {
        DlRescataDatabase dlRescataDatabase = dlRescataDatabaseRepository.findByResourceCodeAndRepositoryType(resourceCode, databaseType);
        // 从数据库连接参数实体中获取数据库连接信息对象
        return this.getDatabaseInfoVoFromDlRescataDatabase(dlRescataDatabase);
    }

    @Override
    public DatabaseInfoVo getDatabaseInfoVoFromDlRescataDatabase(DlRescataDatabase dlRescataDatabase) throws APIException {
        if (dlRescataDatabase == null) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "数据库连接信息不存在");
        }
        // 获取数据库连接的JSON信息
        String databaseJsonInfo = dlRescataDatabase.getDatabaseJsonInfo();
        if (StringUtils.isBlank(databaseJsonInfo)) {
            throw new APIException(APIConstants.CODE_SERVER_ERR, "数据库连接信息JSON不能为空");
        }

        // 解密后的数据库连接JSON信息
        String decryptDatabaseJsonInfo;
        try {
            // 解密连接信息
            decryptDatabaseJsonInfo = basicTextEncryptor.decrypt(databaseJsonInfo);
        } catch (EncryptionOperationNotPossibleException e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, "数据库连接信息解密失败");
        }

        try {
            // 解析数据库连接的JSON信息
            DatabaseInfoVo databaseInfoVo = JSON.parseObject(decryptDatabaseJsonInfo, DatabaseInfoVo.class);
            databaseInfoVo.setDatabaseType(dlRescataDatabase.getDatabaseType());
            return databaseInfoVo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, "数据库连接信息解析为JSON失败");
        }
    }

    @Override
    public List<DlRescataDatabase> findByIsUseForMount(Long isUseForMount) {
        return dlRescataDatabaseRepository.findByIsUseForMount(isUseForMount);
    }

    @Override
    public DlRescataDatabase findByDatabaseId(Long databaseId) {
        Optional<DlRescataDatabase> option = dlRescataDatabaseRepository.findById(databaseId);
        if (option.isPresent()) {
            return option.get();
        }
        return null;
    }
}
