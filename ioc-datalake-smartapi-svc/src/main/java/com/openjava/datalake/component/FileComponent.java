package com.openjava.datalake.component;

import com.commons.utils.MyStringUtils;
import com.openjava.datalake.rabbitMQ.util.conf.RabbitMQHelpBaseConfig;
import com.openjava.minio.component.MinIOComponent;
import com.openjava.minio.vo.FsObjectCatalogVO;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.DataApiResponse;
import org.ljdp.plugin.sys.vo.UserVO;
import org.ljdp.secure.sso.SsoContext;
import org.ljdp.support.attach.component.LjdpDfsUtils;
import org.ljdp.support.attach.component.LjdpFileuploadConfig;
import org.ljdp.support.attach.domain.BsImageFile;
import org.ljdp.support.attach.service.FtpImageFileService;
import org.openjava.boot.tenant.DynamicDataSourceContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * 图片文件上传组件
 * @author lsw & xjd
 *
 */
@Log4j2
@Component
public class FileComponent {

    @Resource
    private FtpImageFileService ftpImageFileService;
    @Resource
    private LjdpDfsUtils dfsUtils;
//    @Resource
//    private WeChatConfigCgp weChatConfigCgp;
    @Resource
    private LjdpFileuploadConfig ljdpFileuploadConfig;
    @Resource
    private MinIOComponent minIOComponent;

    @Resource
    public RabbitMQHelpBaseConfig rabbitMQHelpBaseConfig;

    /**
     * 关联富文本编辑器里面上传的媒体文件（图片，视频）
     * 假如不做这个关联，富文本里面的图片视频可能会定期清理
     * @param bid    业务模型ID
     * @param mediaIds    媒体IDS
     */
    public void uploadFtpImageFile(String bid, String mediaIds) {
        if (StringUtils.isBlank(mediaIds)) {
            return;
        }
        
        String[] items = mediaIds.split(",");
        for (String mid : items) {
            /*
                                 * 第一参数： 附件的ID（前端上传后得到，然后传给后端）
                                 * 第二参数：本条业务记录的id
             */
            ftpImageFileService.doUpdateBid(new Long(mid), bid);
        }
    }
    
    /**
     * 关联保存上传的图片
     * 假如不做这个关联，图片可能会定期清理
     * @param bid   业务ID
     * @param attachIds 附件ids
     * @param fileNum   文件数量(限制作用)
     * @param user  当前登录用户
     * @return 图片路径，多个用英文逗号拼接
     * @throws Exception
     */
    public String uploadFtpImageFile(String bid, String btype, String attachIds, Integer fileNum, UserVO user) throws Exception {
        if (user == null || StringUtils.isBlank(attachIds)) {
            return null;
        }
        
        String[] attachItems = attachIds.split(",");
        if (fileNum == null || fileNum > attachItems.length) {
            fileNum = attachItems.length;
        }
        
        List<String> viewUrlList = new ArrayList<>(fileNum); 
        for (int i= 0; i < fileNum; i++) {
            String aid = attachItems[i];
            //上传附件
            BsImageFile image = ftpImageFileService.upload(aid, btype, bid, (i+1), Long.parseLong(user.getUserId()));
            String viewUrl = dfsUtils.getViewUrl(image);
            viewUrlList.add(viewUrl);
        }
        
        return StringUtils.join(viewUrlList, ",");
    }

    /**
     * 关联保存上传的图片
     * 假如不做这个关联，图片可能会定期清理
     * @param bid   业务ID
     * @param attachIds 附件ids
     * @param fileNum   文件数量(限制作用)
     * @param localMemberId  当前登录用户的本地ID
     * @return 图片路径，多个用英文逗号拼接
     * @throws Exception
     */
    public String uploadFtpImageFileByLocalMemberId(String bid, String btype, String attachIds, Integer fileNum, Long localMemberId) throws Exception {
        if (localMemberId == null || StringUtils.isBlank(attachIds)) {
            return null;
        }

        String[] attachItems = attachIds.split(",");
        if (fileNum == null || fileNum > attachItems.length) {
            fileNum = attachItems.length;
        }

        List<String> viewUrlList = new ArrayList<>(fileNum);
        for (int i= 0; i < fileNum; i++) {
            String aid = attachItems[i];
            //上传附件
            BsImageFile image = ftpImageFileService.upload(aid, btype, bid, (i+1), localMemberId);
            String viewUrl = dfsUtils.getViewUrl(image);
            viewUrlList.add(viewUrl);
        }

        return StringUtils.join(viewUrlList, ",");
    }
    
    /**
     * 关联保存上传的图片
     * 假如不做这个关联，图片可能会定期清理
     * @param bid   业务ID
     * @param attachIds 附件ids
     * @param fileNum   文件数量(限制作用)
     * @param user  当前登录用户
     * @return 图片路径List
     * @throws Exception
     */
    public List<String> uploadFtpImageFileToList(String bid, String btype, String attachIds, Integer fileNum, UserVO user) throws Exception {
        if (user == null || StringUtils.isBlank(attachIds)) {
            return null;
        }
        
        String[] attachItems = attachIds.split(",");
        if (fileNum == null || fileNum > attachItems.length) {
            fileNum = attachItems.length;
        }
        
        List<String> viewUrlList = new ArrayList<>(fileNum); 
        for (int i= 0; i < fileNum; i++) {
            String aid = attachItems[i];
            //上传附件
            BsImageFile image = ftpImageFileService.upload(aid, btype, bid, (i+1), Long.parseLong(user.getUserId()));
            String viewUrl = dfsUtils.getViewUrl(image);
            viewUrlList.add(viewUrl);
        }
        
        return viewUrlList;
    }
    
    public List<String> uploadFtpImageFileToList(String bid, String btype, String attachIds, Integer fileNum, String userId) throws Exception {
        if (userId == null || StringUtils.isBlank(attachIds)) {
            return null;
        }
        
        String[] attachItems = attachIds.split(",");
        if (fileNum == null || fileNum > attachItems.length) {
            fileNum = attachItems.length;
        }
        
        List<String> viewUrlList = new ArrayList<>(fileNum); 
        for (int i= 0; i < fileNum; i++) {
            String aid = attachItems[i];
            //上传附件
            BsImageFile image = ftpImageFileService.upload(aid, btype, bid, (i+1), Long.valueOf(userId));
            String viewUrl = dfsUtils.getViewUrl(image);
            viewUrlList.add(viewUrl);
        }
        
        return viewUrlList;
    }

    /**
     * 删除不必要的图片
     * @param imageIdStr
     */
    public void deleteImages(String imageIdStr) {
        if (StringUtils.isBlank(imageIdStr)) {
            return;
        }
        
        List<Long> imageIdList = MyStringUtils.idsToLongList(imageIdStr);
        try {
            for (Long iamgeId : imageIdList) {
                ftpImageFileService.removeById(iamgeId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 根据业务类型及业务ID删除图片
     * @param btype
     * @param bid
     */
    public void deleteImageByBtypeAndBid(String btype, String bid) {
        try {
            ftpImageFileService.removeByTypeAndBId(btype, bid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 取当前租户code..如果是主湖，则返回null;
     * @return
     */
    public String getSubSiteFromContext() {
    	String adminDepartmentCode = rabbitMQHelpBaseConfig.getCurrentServiceShortName() ;
    	String dbDataSourceId = DynamicDataSourceContextHolder.getDataSource() ;
    	boolean isAdmin = adminDepartmentCode.equalsIgnoreCase(dbDataSourceId) ;
    	if(isAdmin) {
    		return null; //没有切数据源，是主湖
    	}else {
    		return dbDataSourceId; //有切换数据源则是租户的code.
    	}
    }
    
    /**
     * 删除和上传 新增、编辑用
     * @param btype 业务类型
     * @param bid 业务id
     * @param attachIds 上传附件ids
     * @param removeAttachIds 删除附件ids
     * @param memberId 用户id
     */
//    public void removeAndUpload(String btype, Long bid, List<String> attachIds, List<Long> removeAttachIds, Long memberId){
//
//        // 删除附件
//        if (CollectionUtils.isNotEmpty(removeAttachIds)) {
//            for(Long removeAttachId : removeAttachIds){
//                try {
//                    ftpImageFileService.removeById(removeAttachId);
//                } catch (Exception e) {
//                    System.out.println("删除附件失败，opinionId：" + bid + ",removeAttachId:" + removeAttachId );
//                }
//            }
//        }
//
//        // 保存上传附件
//        if(CollectionUtils.isNotEmpty(attachIds)){
//            for (int i = 0; i < attachIds.size(); i++){
//                String localId = attachIds.get(i);
//                File file = null;
//                FileOutputStream fos = null;
//                BufferedOutputStream bos = null;
//                try {
//                    FileItem fileItem = WeixinUtil.downloadFile(localId, weChatConfigCgp.getAppId(), weChatConfigCgp.getAppSecret());
//                    String filePath = ljdpFileuploadConfig.getLocalPath();
//                    byte[] buff = fileItem.getContent();
//                    File dir = new File(filePath);
//                    if (!dir.exists() && dir.isDirectory()){
//                        dir.mkdirs();
//                    }
//                    file = new File(filePath + File.separator + fileItem.getFileName());
//                    fos = new FileOutputStream(file);
//                    bos = new BufferedOutputStream(fos);
//                    bos.write(buff);
//                    BsImageFile imgFile = ftpImageFileService.upload(file, btype, String.valueOf(bid), i+1, memberId);
////                    ftpImageFileService.upload(aid, btype, String.valueOf(bid), i + 1, memberId);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    System.out.println("上传失败，Bid：" + bid + ",aid:" + localId );
//                }finally {
//                    try {
//                        if(bos != null){
//                            bos.close();
//                        }
//                        if(fos != null){
//                            fos.close();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        }
//
//    }
}
