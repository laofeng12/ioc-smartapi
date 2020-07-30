package com.openjava.datalake.smartapi.repository;

import com.openjava.datalake.smartapi.domain.DlApiQuery;
import com.openjava.datalake.smartapi.query.DlApiQueryDBParam;
import org.ljdp.core.spring.data.DynamicJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * DL_API_QUERY数据库访问层
 * @author zjf
 *
 */
public interface DlApiQueryRepository extends DynamicJpaRepository<DlApiQuery, Long>, DlApiQueryRepositoryCustom{

    /**
     * 获取接口名称已存在数量
     * @param queryid 不等于 接口ID
     * @param queryName 接口名称
     * @param deleteMark 删除状态：0正常
     * @param validMark 启用状态：1有效
     * @return 返回接口名称存在的总数量
     */
    int countByQueryIdNotAndQueryNameAndDeleteMarkAndValidMark(Long queryid, String queryName, Long deleteMark, Long validMark);

    /**
     * 获取相同接口名称简称的数量
     * @param queryid 不等于 接口ID
     * @param queryLogogram 接口名称简称
     * @return 相同简称的总数量
     */
    int countByQueryIdNotAndQueryLogogram(Long queryid, String queryLogogram);

    /**
     * 根据查询接口编号获取接口详情
     * @param queryCode 查询接口编号
     * @param deleteMark 是否删除 0正常
     * @param validMark 是否有效 1有效
     * @return
     */
    DlApiQuery findByQueryCodeAndDeleteMarkAndValidMark(String queryCode, Long deleteMark, Long validMark);

    /**
     * 获取资源目录所有相关查询API
     * @param resourceCode
     * @return
     */
    List<DlApiQuery> findByResourceCode(String resourceCode);
    /**
     * 根据资源目录编码获取接口详情
     * 按照最先修改时间来排序，修改时间为空的就按照创建时间来排序
     * @param resourceCode 查询接口编号
     * deleteMark 是否删除 0正常
     * validMark 是否有效 1有效
     * @return
     */
    @Query("from DlApiQuery where resourceCode = :resourceCode and deleteMark = 0 and validMark = 1 " +
            "order by modifyDate desc nulls first , createDate desc ")
    List<DlApiQuery> findByResourceCodeAndEffectOrderByModifyDate(String resourceCode);

    /**
     * 查找创建者|提供者|有权限是当前用户的所有api
     */
    @Query(value="select q.* from Dl_Rescata_Resource r "
               +"inner join Dl_Api_Query q on q.resource_Code=r.resource_Code "
               +"and q.DELETE_MARK=:#{#params.eq_deleteMark} "
               +"and q.VALID_MARK= 1 "
               +"and (q.QUERY_NAME like :#{#params.like_queryName} or :#{#params.like_queryName} is null) "
               +"and (r.RESOURCE_NAME like :#{#params.like_resourceName} or :#{#params.like_resourceName} is null) "
               +"where r.IS_LATEST= 1 " +
            "       and q.CREATE_USER_ACCOUNT=:#{#params.eq_createUserAccount} " +
            // 只用查出自己创建的接口
//            "   and (r.business_Owner_Account=:#{#params.eq_createUserAccount} "
//               +"or r.create_Account=:#{#params.eq_createUserAccount} " +
//            "    or "
//               +"r.resource_Code in (select rp.resource_Code from DL_RESCATA_RESOURCE_PERMISSION rp " +
//            "           where rp.owner_Account=:#{#params.eq_createUserAccount}  and permission_Level < 3) " +
//            ") " +
            " " +
                "ORDER BY q.CREATEDATE DESC ",
            countQuery = "select rownum from Dl_Rescata_Resource r "+
                    "inner join Dl_Api_Query q on q.resource_Code=r.resource_Code and q.DELETE_MARK=:#{#params.eq_deleteMark} "
                    +"and q.VALID_MARK= 1 "
                    +"and (q.QUERY_NAME like :#{#params.like_queryName} or :#{#params.like_queryName} is null) "
                    +"and (r.RESOURCE_NAME like :#{#params.like_resourceName} or :#{#params.like_resourceName} is null) "
                    +"where r.IS_LATEST= 1 " +
                    "       and q.CREATE_USER_ACCOUNT=:#{#params.eq_createUserAccount} " +
                    // 只用查出自己创建的接口
//            "   and (r.business_Owner_Account=:#{#params.eq_createUserAccount} "
//               +"or r.create_Account=:#{#params.eq_createUserAccount} " +
//            "    or "
//               +"r.resource_Code in (select rp.resource_Code from DL_RESCATA_RESOURCE_PERMISSION rp " +
//            "           where rp.owner_Account=:#{#params.eq_createUserAccount}  and permission_Level < 3) " +
//            ") " +
                    " ",
            nativeQuery = true)
     Page<DlApiQuery> queryMaybeAccessedApi(DlApiQueryDBParam params, Pageable pageable);

    DlApiQuery findByQueryCode(String queryCode);


    List<DlApiQuery> findByResourceIdAndCreateUserAccountAndIsFullRespAndDeleteMarkAndValidMark(Long resourceId, String userAccount, Long isFullResp,
                                                                                                Long deleteMark, Long validMark);
}
