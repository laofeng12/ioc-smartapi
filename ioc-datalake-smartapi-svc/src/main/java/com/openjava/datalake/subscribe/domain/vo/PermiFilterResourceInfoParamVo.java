package com.openjava.datalake.subscribe.domain.vo;

import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.domain.DlRescataResource;
import com.openjava.datalake.rescata.domain.DlRescataStrucPermi;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author xjd
 * @Date 2020/7/14 10:39
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermiFilterResourceInfoParamVo {

    @NotNull
    private List<DlRescataColumn> allRescataColumnList;
    @NotNull
    private DlRescataResource dlRescataResource;
    @NotNull
    private List<DlRescataStrucPermi> structurePermissions;

}
