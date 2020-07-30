package com.openjava.datalake.dataxjdbcutil.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * @Author xjd
 * @Date 2019/9/2 20:25
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class TableContext {

    private String tableName;
    private List<ColumnInfo> columnInfos;
    private List<? extends Object[]> dataRows;
    private Boolean deleteAll;
    private Boolean needUpdate;
    private String pkColumnName;
    private Boolean needDelete;
}
