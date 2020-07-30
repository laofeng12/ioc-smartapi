package com.openjava.datalake.smartapi.vo;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 资源目录表信息
 *
 * @Author heziyou
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTableVO implements Serializable {

    private DlRescataResource dlRescataResource;
    private DatabaseInfoVo databaseInfoVo;
    private List<DlRescataColumn> dlRescataColumnList;
    private List<DlRescataStrucPermi> structurePermissions;
}
