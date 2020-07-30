package com.openjava.datalake.rescata.repository;

import com.openjava.datalake.rescata.domain.DlResourceUnstructuredFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @Author JiaHai
 * @Description 资源目录非结构化文件关系表 数据库访问层
 */
public interface DlResourceUnstructuredFileRepository extends JpaRepository<DlResourceUnstructuredFile, Long> {
//    /**
//     * 根据资源目录ID查询BsImageFileVo
//     *
//     * @param resourceId
//     * @return
//     */
//    @Query("SELECT new com.openjava.datalake.base.vo.BsImageFileVo(b.id, b.btype, b.bid, b.picname, b.picurl, b.seqno, " +
//            "b.userid, b.creatime, b.smallpic, b.n1pic, b.bucketname, b.filesize, b.duration, b.filekey, b.objectkey, " +
//            "r.resourceId, r.openScope, r.openScopeString, r.id )" +
//            " FROM DlBsImageFile b, DlResourceUnstructuredFile r" +
//            " WHERE b.id = r.bsImageFileId" +
//            "   AND r.resourceId = :resourceId")
//    List<BsImageFileVo> findBsImageFileVoByResourceId(Long resourceId);

    /**
     * 根据一组ID查询
     *
     * @param idList
     * @return
     */
    @Query("FROM DlResourceUnstructuredFile WHERE id IN :idList")
    List<DlResourceUnstructuredFile> findByIdIn(List<Long> idList);

    /**
     * 根据资源目录ID查询
     *
     * @param resourceId 资源目录ID
     * @return
     */
    List<DlResourceUnstructuredFile> findByResourceId(Long resourceId);

    /**
     * 根据信息资源编码查询
     *
     * @param resourceCode 信息资源编码
     * @return
     */
    List<DlResourceUnstructuredFile> findByResourceCode(String resourceCode);

    /**
     * 清除旧的文件列表，用于文件列表同步
     * @param resourceCode
     */
    void deleteAllByResourceCode(String resourceCode);
}
