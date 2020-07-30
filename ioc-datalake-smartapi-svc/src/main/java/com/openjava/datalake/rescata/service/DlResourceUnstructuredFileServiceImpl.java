package com.openjava.datalake.rescata.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.openjava.datalake.base.dto.ResourceFileOutputDTO;
import com.openjava.datalake.common.FileUploadConstant;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.component.FileComponent;
import com.openjava.datalake.rabbitMQ.util.conf.RabbitMQHelpBaseConfig;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlResourceUnstructuredFile;
import com.openjava.datalake.rescata.repository.DlResourceUnstructuredFileRepository;
import com.openjava.datalake.subscribe.service.DlSubscribeCatalogFormService;
import com.openjava.minio.component.MinIOComponent;
import com.openjava.minio.vo.FsObjectCatalogVO;
import com.openjava.util.IdUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.component.result.GeneralResult;
import org.ljdp.component.result.Result;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.support.attach.vo.AttachVO;
import org.ljdp.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author JiaHai
 * @Description 资源目录非结构化文件关系表 业务层接口实现类
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DlResourceUnstructuredFileServiceImpl implements DlResourceUnstructuredFileService {
    @Autowired
    private DlResourceUnstructuredFileRepository dlResourceUnstructuredFileRepository;

    @Autowired
    private DlRescataResourceService dlRescataResourceService;

    @Autowired
    private MinIOComponent minIOComponent;

    @Autowired
    //@Lazy
    private DlSubscribeCatalogFormService dlSubscribeCatalogFormService;

    @Resource
    private DlRescataResourcePermissionService dlRescataResourcePermissionService;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Resource
    private RabbitMQHelpBaseConfig rabbitMQHelpBaseConfig;
    @Autowired
    private DataCodeService dataCodeService;
    @Resource
    private FileComponent fileComponent;

    /**
     * 不过滤权限，查全部， 但是置空下载地址
     * todo 待优化，不连minIO
     *
     * @param resourceCode
     * @return
     * @throws APIException
     */
    @Override
    public List<ResourceFileOutputDTO> getFileListByResourceCode(String resourceCode) throws APIException {
        // 获取最新目录信息
        DlRescataResource dlRescataResource = getLatestRescataByResourceCode(resourceCode);
        // 获取 资源目录的公开范围
        Long openScope = dlRescataResource.getOpenScope();

        // 3、根据资源目录ID 查询附件信息（关联）
        List<DlResourceUnstructuredFile> dlResourceUnstructuredFileList = dlResourceUnstructuredFileRepository.findByResourceCode(resourceCode);
        // 4、根据业务类型，查询附件列表（物理）
        String subSite = fileComponent.getSubSiteFromContext();//是否租户.
        DataApiResponse<FsObjectCatalogVO> dataApiResponse = minIOComponent.search(resourceCode,subSite);
        if (null == dataApiResponse.getCode() || APIConstants.CODE_SUCCESS == dataApiResponse.getCode()) {
            // 获取附件列表（物理）
            List<FsObjectCatalogVO> fsObjectCatalogVOList = dataApiResponse.getRows();

            // 5、转换 返回对象List
            List<ResourceFileOutputDTO> resourceFileOutputDTOList = this.convertResourceFileOutputDTOList(dlResourceUnstructuredFileList, fsObjectCatalogVOList, openScope);
            // 置空下载地址，只展示文件名
            for (ResourceFileOutputDTO resourceFileOutputDTO : resourceFileOutputDTOList) {
                resourceFileOutputDTO.setViewUrl(null);
                resourceFileOutputDTO.setDownloadUrl(null);
            }
            return resourceFileOutputDTOList;
        } else {
            throw new APIException(APIConstants.CODE_SERVER_ERR, "附件上传组件内部调用错误");
        }
    }

    /**
     * 获取最新版本目录信息
     * @param resourceCode 目录编号
     * @return dlRescataResource
     * @throws APIException
     */
    private DlRescataResource getLatestRescataByResourceCode(String resourceCode) throws APIException {
        // 1、校验 参数非空
        if (StringUtils.isBlank(resourceCode)) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "信息资源编码 不可为空");
        }
        // 2、资源目录最新版是否存在（resourceCode是否有效）
        DlRescataResource dlRescataResource = dlRescataResourceService.queryLatestByResourceCode(resourceCode);
        if (null == dlRescataResource) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "该信息资源编码有误，目录不存在");
        }
        return dlRescataResource;
    }

    /**
     * 非结构化附件查询 返回对象List 转换
     *
     * @param dlResourceUnstructuredFileList 附件关系列表（逻辑关联）
     * @param fsObjectCatalogVOList          附件列表（物理）
     * @param openScope                      公开范围
     * @return
     * @throws APIException
     */
    private List<ResourceFileOutputDTO> convertResourceFileOutputDTOList(List<DlResourceUnstructuredFile> dlResourceUnstructuredFileList, List<FsObjectCatalogVO> fsObjectCatalogVOList, Long openScope) throws APIException {
        if (CollectionUtils.isEmpty(dlResourceUnstructuredFileList)) {
            return Collections.EMPTY_LIST;
        }
        // 获取 附件的公开范围（同步 资源目录的公开范围）
        Long attachmentOpenScope = openScope;
        // 翻译 附件的公开范围
        String openScopeString = dataCodeService.findCodeNameByCodetypeAndCodeFromCache(ResourceConstant.COLUMN_OPEN_SCOPE, attachmentOpenScope);

        // 创建并初始化返回对象List
        List<ResourceFileOutputDTO> resourceFileOutputDTOList = new ArrayList<>(dlResourceUnstructuredFileList.size());
        for (DlResourceUnstructuredFile dlResourceUnstructuredFile : dlResourceUnstructuredFileList) {
            // 创建返回对象
            ResourceFileOutputDTO resourceFileOutputDTO = new ResourceFileOutputDTO();
            resourceFileOutputDTO.setId(dlResourceUnstructuredFile.getId()).setOpenScopeString(openScopeString);
            fsObjectCatalogVOList.stream().forEach(fsObjectCatalogVO -> {
                if (Objects.equals(dlResourceUnstructuredFile.getObjectKey(), fsObjectCatalogVO.getObjectkey())) {
                    // 附件名
                    String filename = fsObjectCatalogVO.getFilename();
                    // 预览URL
                    String viewPath = fsObjectCatalogVO.getViewPath();
                    // 下载URL
                    String downloadPath = fsObjectCatalogVO.getDownloadPath();
                    //文件类型
                    String fileType = translateFileType(fsObjectCatalogVO.getBtype());
                    //大小
                    String fileSize = calcFileSize(fsObjectCatalogVO.getFilesize());
                    //上传人
                    String uploadUser = fsObjectCatalogVO.getUseraccount();
                    //上传时间
//                    String uploadTime = new DateTime(fsObjectCatalogVO.getCreatime()).toString("yyyy-MM-dd HH:mm:ss");
                    // 赋值
                    resourceFileOutputDTO.setFileType(fileType).setFileSize(fileSize)
                            .setUploadUser(uploadUser).setUploadTime(null)
                            .setName(filename).setViewUrl(viewPath).setDownloadUrl(downloadPath);
                }
            });
            resourceFileOutputDTOList.add(resourceFileOutputDTO);
        }

        // 返回结果
        return resourceFileOutputDTOList;
    }
    /**
     * 翻译文件类型
     * @param businessType 业务类型
     * @return
     */
    private String translateFileType(String businessType) {
        Long fileTypeCode = null;
        if(FileUploadConstant.RESOURCE_CATALOG_TEXT.equals(businessType)) {
            fileTypeCode = PublicConstant.TXT;
        }else if(FileUploadConstant.RESOURCE_CATALOG_PICTURE.equals(businessType)) {
            fileTypeCode = PublicConstant.PICTURE;
        }else if(FileUploadConstant.RESOURCE_CATALOG_AUDIO.equals(businessType)) {
            fileTypeCode = PublicConstant.AUDIO;
        }else if(FileUploadConstant.RESOURCE_CATALOG_VIDEO.equals(businessType)) {
            fileTypeCode = PublicConstant.VIDEO;
        }
        return fileTypeCode == null ? null : PublicConstant.dataDictionaryCacheMap.get(PublicConstant.DL_RESOURCE_FILETYPE).get(fileTypeCode.toString());
    }
    /**
     * 翻译文件大小
     * @param fileSize 文件大小（字节）
     * @return
     */
    private String calcFileSize(Long fileSize) {
        if(fileSize >= 1024 && fileSize < 1024*1024) {
            return keepPrecision(fileSize / 1024d, 2) + "KB";
        }else if(fileSize >= 1024*1024 && fileSize < 1024*1024*1024) {
            return keepPrecision(fileSize / 1024*1024d, 2) + "M";
        }else if(fileSize >= 1024*1024*1024) {
            return keepPrecision(fileSize / 1024*1024*1024d, 2) + "G";
        }
        return fileSize + "B";
    }
    private double keepPrecision(double number, int precision) {
        BigDecimal bg = new BigDecimal(number);
        return bg.setScale(precision, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

}
