package com.openjava.datalake.util;

import com.openjava.admin.user.vo.OaOrgVO;
import com.openjava.admin.user.vo.OaUserVO;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.ljdp.secure.sso.SsoContext;

/**
 * 当前登录用户信息
 *
 * @author hqr
 * @date 2020/5/25
 */
public class UserInfoUtils {

    /**
     * 获取当前登录对象的父级对象
     * @return
     * @throws APIException
     */
    public static OaOrgVO getOaOrgVO() throws APIException {
        LoginUtils.validateLogin();
        OaUserVO oaUserVO = (OaUserVO) SsoContext.getUser();
        OaOrgVO topOrg = oaUserVO.getTopOrg();
        if (topOrg == null) {
            throw new APIException(APIConstants.ACCOUNT_NO_LOGIN, "当前登录用户,无法获取到局信息");
        }
        return topOrg;
    }

    /**
     * 获取当前登录对象
     * @return
     * @throws APIException
     */
    public static OaUserVO getOaUserVO() throws APIException {
        LoginUtils.validateLogin();
        OaUserVO oaUserVO = (OaUserVO) SsoContext.getUser();
        return oaUserVO;
    }

    /**
     * 获取当前登录用户 顶级机构编码createDeptTopCode
     * @return
     * @throws APIException
     */
    public static String getCreateDeptTopCode() throws APIException {
        OaOrgVO topOrg = getOaOrgVO();
        return topOrg.getDeptcode();
    }

    /**
     * 获取当前登录用户 顶级机构CreateDeptTopId
     * @return
     * @throws APIException
     */
    public static String getCreateDeptTopId() throws APIException {
        OaOrgVO oaOrgVO = getOaOrgVO();
        return oaOrgVO.getOrgid();
    }

    /**
     * 获取当前登录用户 顶级机构CreateDeptTopName
     * @return
     * @throws APIException
     */
    public static String getCreateDeptTopName() throws APIException {
        OaOrgVO oaOrgVO = getOaOrgVO();
        return oaOrgVO.getOrgname();
    }

    /**
     * 获取当前登录部门 机构编码createDeptCode
     * @return
     * @throws APIException
     */
    public static String getCreateDeptCode() throws APIException {
        OaUserVO oaUserVO = getOaUserVO();
        return oaUserVO.getDeptcode();
    }

}
