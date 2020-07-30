package com.openjava.datalake.util;

import com.alibaba.fastjson.JSON;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author JiaHai
 * @Description 数据库DDL SQL相关
 */
public class DdlSqlUtils {
    @Value("${datalake.is-cluster:false}")
    private String isCluster;

    /**
     * HIVE —— 短字符/较长字符/长字符
     */
    public static final String HIVE_DATATYPE_STRING = "STRING";
    /**
     * HIVE —— 日期型
     */
    public static final String HIVE_DATATYPE_DATE = "DATE";
    /**
     * HIVE —— 整数型
     */
    public static final String HIVE_DATATYPE_INTEGER = "BIGINT";
    /**
     * HIVE —— 小数型
     */
    public static final String HIVE_DATATYPE_DECIMAL = "DOUBLE";

    /**
     * PostgreSQL —— 短字符/较长字符/长字符
     */
    public static final String POSTGRESQL_DATATYPE_STRING = "VARCHAR";
    /**
     * PostgreSQL —— 日期型
     */
    public static final String POSTGRESQL_DATATYPE_DATE = "TIMESTAMP(6)";
    /**
     * PostgreSQL —— 整数型
     */
    public static final String POSTGRESQL_DATATYPE_INTEGER = "INT8";
    /**
     * PostgreSQL —— 小数型
     */
    public static final String POSTGRESQL_DATATYPE_DECIMAL = "NUMERIC(30,10)";

    /**
     * Oracle —— 短字符/较长字符/长字符
     */
    public static final String ORACLE_DATATYPE_SHORT_STRING = "VARCHAR2(200)";
    /**
     * Oracle —— 短字符/较长字符/长字符
     */
    public static final String ORACLE_DATATYPE_MIDDLE_STRING = "VARCHAR2(1000)";
    /**
     * Oracle —— 短字符/较长字符/长字符
     */
    public static final String ORACLE_DATATYPE_LONG_STRING = "VARCHAR2(4000)";
    /**
     * Oracle —— 日期型
     */
    public static final String ORACLE_DATATYPE_DATE = "DATE";
    /**
     * Oracle —— 整数型
     */
    public static final String ORACLE_DATATYPE_INTEGER = "NUMBER(18)";
    /**
     * Oracle —— 小数型
     */
    public static final String ORACLE_DATATYPE_DECIMAL = "NUMBER(18,8)";

    /**
     * 生成 建表SQL
     *
     * @param databaseInfoVo      数据库连接信息
     * @param databaseType        数据库类型
     * @param tableName           表名
     * @param dlRescataColumnList 字段信息列表
     * @return
     * @throws APIException
     */
    public static String createTableSql(DatabaseInfoVo databaseInfoVo, Long databaseType, String tableName, List<DlRescataColumn> dlRescataColumnList, String isCluster) throws APIException {
        if (null == databaseType) {
            throw new APIException("databaseType 不可为空");
        }
        if (StringUtils.isBlank(tableName)) {
            throw new APIException("tableName 不可为空");
        }
        String sql = "CREATE TABLE ";
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            sql += tableName;
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            if (null == databaseInfoVo) {
                throw new APIException("databaseInfo 不可为空");
            }
            sql += "\"" + databaseInfoVo.getSchema() + "\"." + tableName;
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            if (null == databaseInfoVo) {
                throw new APIException("databaseInfo 不可为空");
            }
            sql += tableName;
        }
        List<String> columnSqlList = columnSql(databaseType, dlRescataColumnList);
        sql += "( " + String.join(", ", columnSqlList) + " )";

        // PostgreSQL增加分布式列
        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType) && "true".equals(isCluster)) {
            for (DlRescataColumn dlRescataColumn : dlRescataColumnList) {
                if (PublicConstant.YES.equals(dlRescataColumn.getIsPrimaryKey())) {
                    sql = sql + " DISTRIBUTE BY HASH ( " + dlRescataColumn.getColumnDefinition() + " ) ";
                }
            }
        }

        System.out.println("———————————————— create table SQL ");
        System.out.println(sql);
        return sql;
    }

    /**
     * 建表SQL中的 字段部分SQL
     *
     * @param databaseType        数据库类型
     * @param dlRescataColumnList 字段信息列表
     * @return
     * @throws APIException
     */
    private static List<String> columnSql(Long databaseType, List<DlRescataColumn> dlRescataColumnList) throws APIException {
        if (CollectionUtils.isEmpty(dlRescataColumnList)) {
            throw new APIException("字段不可为空");
        }
        List<String> columnSqlList = new ArrayList<>();
        dlRescataColumnList.stream().forEach(dlRescataColumn -> {
            try {
                String columnSql = " " + dlRescataColumn.getColumnDefinition().toLowerCase() + " ";
                // 数据类型、长度
                String dataType = dataTypeConvert(databaseType, dlRescataColumn.getDataType());
                if (POSTGRESQL_DATATYPE_STRING.equalsIgnoreCase(dataType)) {
                    if (null != dlRescataColumn.getColumnLength() && dlRescataColumn.getColumnLength() > 0) {
                        columnSql += dataType + "(" + dlRescataColumn.getColumnLength() + ")";
                    } else {
                        columnSql += dataType;
                    }
                } else {
                    columnSql += dataType;
                }
                // 不可为空？
                if (PublicConstant.YES.equals(dlRescataColumn.getIsRequire())) {
                    columnSql += " NOT NULL";
                }
                columnSqlList.add(columnSql);
            } catch (APIException e) {
                e.printStackTrace();
            }
        });
        return columnSqlList;
    }

    /**
     * 根据数据库类型，对字段宏观数据类型赋值
     *
     * @param databaseType 数据库类型
     * @param dataType     字段宏观类型
     * @return
     * @throws APIException
     */
    public static String dataTypeConvert(Long databaseType, Long dataType) throws APIException {
        if (null == dataType) {
            throw new APIException("dataType 不可为空");
        }
        String converDataType = "";
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            if (ResourceConstant.COLUMN_DATATYPE_SHORT_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_LONG_STRING.equals(dataType)) {
                converDataType = HIVE_DATATYPE_STRING;
            } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                converDataType = HIVE_DATATYPE_DATE;
            } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                converDataType = HIVE_DATATYPE_INTEGER;
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                converDataType = HIVE_DATATYPE_DECIMAL;
            }
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            if (ResourceConstant.COLUMN_DATATYPE_SHORT_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_LONG_STRING.equals(dataType)) {
                converDataType = POSTGRESQL_DATATYPE_STRING;
            } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                converDataType = POSTGRESQL_DATATYPE_DATE;
            } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                converDataType = POSTGRESQL_DATATYPE_INTEGER;
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                converDataType = POSTGRESQL_DATATYPE_DECIMAL;
            }
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            if (ResourceConstant.COLUMN_DATATYPE_SHORT_STRING.equals(dataType)) {
                converDataType = ORACLE_DATATYPE_SHORT_STRING;
            } else if (ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING.equals(dataType)) {
                converDataType = ORACLE_DATATYPE_MIDDLE_STRING;
            } else if (ResourceConstant.COLUMN_DATATYPE_LONG_STRING.equals(dataType)) {
                converDataType = ORACLE_DATATYPE_LONG_STRING;
            } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                converDataType = ORACLE_DATATYPE_DATE;
            } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                converDataType = ORACLE_DATATYPE_INTEGER;
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                converDataType = ORACLE_DATATYPE_DECIMAL;
            }
        }
        return converDataType;
    }

    /**
     * 生成 设置表的主键SQL
     *
     * @return
     */
    public static String setPrimaryKeySql(Long databaseType, String tableName, List<DlRescataColumn> dlRescataColumnList) {
        StringBuilder sb = new StringBuilder();
        for (DlRescataColumn dlRescataColumn : dlRescataColumnList) {
            if (PublicConstant.YES.equals(dlRescataColumn.getIsPrimaryKey())) {
                // 主键
                if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
                    // TODO HIVE表 无主键
                } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
                    sb.append("ALTER TABLE " + tableName + " ADD PRIMARY KEY( " + dlRescataColumn.getColumnDefinition() + " )");
                } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
                    sb.append("ALTER TABLE " + tableName + " CONSTRAINT " + tableName + "_PK PRIMARY KEY (" + dlRescataColumn.getColumnDefinition() + " )");
                }
                System.out.println("set primary key of table： " + sb.toString());
                return sb.toString();
            }
        }
        return null;
    }

    /**
     * 生成 给数据库表添加注释的SQL
     *
     * @param databaseType 数据库类型
     * @param tableName    表名
     * @param tableComment 表注释
     * @return
     */
    public static String commentTableSql(Long databaseType, String tableName, String tableComment) {
        String sql = null;
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {

        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType) || ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            sql = "COMMENT ON TABLE " + tableName + " IS '" + tableComment + "'";
        }
        System.out.println("comment on table： " + sql);
        return sql;
    }

    /**
     * 生成 添加字段注释的SQL
     *
     * @param databaseType        数据库类型
     * @param tableName           表名
     * @param dlRescataColumnList 字段信息列表
     * @return
     */
    public static List<String> commentColumnSql(Long databaseType, String tableName, List<DlRescataColumn> dlRescataColumnList) {
        List<String> commentColumnSqlList = new ArrayList<>();
        if (databaseType.equals(ResourceConstant.DATABASE_TYPE_HIVE)) {

        } else if (databaseType.equals(ResourceConstant.DATABASE_TYPE_MPP) || databaseType.equals(ResourceConstant.DATABASE_TYPE_ORACLE)) {
            dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                if (StringUtils.isNotBlank(dlRescataColumn.getColumnComment())) {
                    commentColumnSqlList.add("COMMENT ON COLUMN " + tableName + "." + dlRescataColumn.getColumnDefinition().toLowerCase() + " IS '" + dlRescataColumn.getColumnComment() + "'");
                }
            });
        }
        System.out.println("add comment on column：" + JSON.toJSON(commentColumnSqlList));
        return commentColumnSqlList;
    }

    /**
     * 生成 添加字段默认值的SQL
     *
     * @param databaseType        数据库类型
     * @param tableName           表名
     * @param dlRescataColumnList 字段信息列表
     * @return
     */
    public static List<String> addColumnDefaultSql(Long databaseType, String tableName, List<DlRescataColumn> dlRescataColumnList) {
        List<String> commentColumnSqlList = new ArrayList<>();
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
//            dlRescataColumnList.stream().forEach(dlRescataColumn -> commentColumnSqlList.add("ALTER TABLE " + tableName + " ADD COLUMNS( " + dlRescataColumn.getColumnDefinition() + " " + type +" COMMENT '" + dlRescataColumn.getColumnComment() + "')"));
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                if (StringUtils.isNotBlank(dlRescataColumn.getDefaultValue())) {
                    commentColumnSqlList.add("ALTER TABLE " + tableName + " ALTER COLUMN " + dlRescataColumn.getColumnDefinition().toLowerCase() + " SET DEFAULT '" + dlRescataColumn.getDefaultValue() + "'");
                }
            });
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                if (StringUtils.isNotBlank(dlRescataColumn.getDefaultValue())) {
                    commentColumnSqlList.add("ALTER TABLE " + tableName + " MODIFY " + dlRescataColumn.getColumnDefinition().toLowerCase() + " DEFAULT '" + dlRescataColumn.getDefaultValue() + "'");
                }
            });
        }
        System.out.println("add default on column：" + JSON.toJSON(commentColumnSqlList));
        return commentColumnSqlList;
    }

    /**
     * 生成 添加字段索引的SQL
     *
     * @param databaseType        数据库类型
     * @param tableName           表名
     * @param dlRescataColumnList 字段信息列表
     * @return
     */
    public static List<String> addColumnIndexSql(Long databaseType, String tableName, List<DlRescataColumn> dlRescataColumnList) {
        // 筛选出用于查询条件的信息项（去除主键）
        dlRescataColumnList = dlRescataColumnList.stream().filter(dlRescataColumn -> PublicConstant.YES.equals(dlRescataColumn.getIsQuery()) && !PublicConstant.YES.equals(dlRescataColumn.getIsPrimaryKey())).collect(Collectors.toList());
        List<String> columnIndexSqlList = new ArrayList<>();
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {

        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                // 字段名称
                String columnDefinition = dlRescataColumn.getColumnDefinition().toLowerCase();
                // 索引名称
                String indexName = tableName + "_" + columnDefinition + "_" + RandomStringUtils.randomAlphabetic(6).toLowerCase();
                // 索引方法（字符串类型使用 GIN）（普通使用B-Tree）（PostgreSQL 使用非 BTREE 添加索引时经常遇到异常，暂定均使用BTREE）
//                String indexMethod = dlRescataColumn.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE ? "GIN" : "BTREE";
                String indexMethod = "BTREE";
                columnIndexSqlList.add("CREATE INDEX " + indexName + " ON " + tableName + " USING " + indexMethod + " (" + columnDefinition + ")");
            });
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                // 字段名称
                String columnDefinition = dlRescataColumn.getColumnDefinition().toLowerCase();
                // 索引名称
                String indexName = tableName + "_" + columnDefinition + "_" + RandomStringUtils.randomAlphabetic(6).toLowerCase();
                columnIndexSqlList.add("CREATE INDEX " + indexName + " ON " + tableName + " (" + columnDefinition + ")");
            });
        }
        System.out.println("create index on column：" + JSON.toJSON(columnIndexSqlList));
        return columnIndexSqlList;
    }

    /**
     * 索引SQL生成：创建索引
     * 1.MPP库：isUsingGIN 才有效
     * 2.通用使用默认B-Tree索引(MPP/Oracle)
     *
     * @param databaseInfoVo 数据库信息
     * @param tableName      表名称
     * @param columnName     字段名
     * @param isUsingGIN     是否使用GIN索引 true:MPP类型&&字段为字符类型
     * @return 索引SQL
     */
    public static String createIndexSql(DatabaseInfoVo databaseInfoVo, String tableName, String columnName, Boolean isUsingGIN) {
        //构造返回对象
        StringBuilder indexSql = new StringBuilder("CREATE INDEX IDX_DATALAKE_");
        //判断数据库类型
        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseInfoVo.getDatabaseType())) {
            //MPP数据库
            //1.字符类型且使用模糊查询时，使用GIN创建索引；2.其他的使用b-tree索引即可
            StringBuilder tableSchema = new StringBuilder("\"").append(databaseInfoVo.getSchema())
                    .append("\".\"").append(tableName).append("\"");
            if (isUsingGIN) {
                //gin索引
                indexSql.append(tableName).append("_").append(columnName).append(" ON ")
                        .append(tableSchema).append(" USING GIN(").append(columnName).append(")");
            } else {
                //b-tree索引
                indexSql.append(tableName).append("_").append(columnName).append(" ON ")
                        .append(tableSchema).append(" USING BTREE (").append(columnName).append(")");
            }
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseInfoVo.getDatabaseType())) {
            //Oracle 默认使用b-tree索引
            indexSql.append(tableName).append("_").append(columnName).append(" ON ")
                    .append(tableName).append("(").append(columnName).append(")");
        }
        System.out.println("sql index:" + JSON.toJSON(indexSql.toString()));
        return indexSql.toString();
    }

    /**
     * 索引SQL生成：查询某个表某个索引是否存在
     *
     * @param databaseInfoVo 数据库信息
     * @param tableName      数据库名
     * @param columnName     索引名称
     * @return select count(*) from
     */
    public static String selectAndCountIndexSql(DatabaseInfoVo databaseInfoVo, String tableName, String columnName) {
        //构造返回对象
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM ");
        StringBuilder indexName = new StringBuilder("IDX_DATALAKE_").append(tableName).append("_").append(columnName);
        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseInfoVo.getDatabaseType())) {
            //MPP库
            sql.append("PG_INDEXES WHERE TABLENAME='").append(tableName).append("' AND INDEXNAME='")
                    .append(indexName).append("'");
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseInfoVo.getDatabaseType())) {
            //Oracle库
            sql.append("USER_INDEXES WHERE TABLE_NAME='").append(tableName).append("' AND INDEX_NAME='")
                    .append(indexName).append("'");
        }
        System.out.println("select count sql:" + sql.toString());
        return sql.toString();
    }


    /**
     * 索引SQL生成：删除索引
     *
     * @param databaseInfoVo 数据库信息
     * @param tableName      数据库名
     * @param columnName     索引名称
     * @return drop index
     */
    public static String deleteIndexSql(DatabaseInfoVo databaseInfoVo, String tableName, String columnName) {
        //构造返回对象
        StringBuilder sql = new StringBuilder();
        StringBuilder indexName = new StringBuilder("idx_datalake_").append(tableName).append("_").append(columnName);
        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseInfoVo.getDatabaseType())) {
            sql.append("DROP INDEX IF EXISTS \"").append(databaseInfoVo.getSchema()).append("\".\"").append(indexName).append("\"");
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseInfoVo.getDatabaseType())) {
            sql.append("DROP INDEX ").append(indexName);
        }
        System.out.println(sql.toString());
        return sql.toString();
    }

    /**
     * 补充字段，为归集库、中心库建表使用
     *
     * @param schema
     * @param tableName
     * @return
     */
    public static List<String> supplyForHJAndZX(String schema, String tableName, Long databaseType) {
        List<String> list = new ArrayList<>();
        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            // PostgreSQL
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD id_tokimnhj VARCHAR(400)");
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD dir_id_tokimnhj VARCHAR(100)");
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD addtime_tokimnhj TIMESTAMP");
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD updatetime_tokimnhj TIMESTAMP");
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD state_tokimnhj VARCHAR(50)");
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD hash_tokimnhj VARCHAR(500)");
            list.add("ALTER TABLE \"" + schema + "\"." + tableName + " ADD data_hash_tokimnhj TIMESTAMP");

            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".id_tokimnhj IS 'ID'");
            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".dir_id_tokimnhj IS '目录ID'");
            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".addtime_tokimnhj IS '增加时间'");
            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".updatetime_tokimnhj IS '更新时间'");
            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".state_tokimnhj IS '状态'");
            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".hash_tokimnhj IS '防窜改码'");
            list.add("COMMENT ON COLUMN \"" + schema + "\"." + tableName + ".data_hash_tokimnhj IS '防窜改码时间'");
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            // Oracle
            list.add("ALTER TABLE " + tableName + " ADD id_tokimnhj VARCHAR(400)");
            list.add("ALTER TABLE " + tableName + " ADD dir_id_tokimnhj VARCHAR2(100)");
            list.add("ALTER TABLE " + tableName + " ADD addtime_tokimnhj DATE");
            list.add("ALTER TABLE " + tableName + " ADD updatetime_tokimnhj DATE");
            list.add("ALTER TABLE " + tableName + " ADD state_tokimnhj VARCHAR2(50)");
            list.add("ALTER TABLE " + tableName + " ADD hash_tokimnhj VARCHAR2(500)");
            list.add("ALTER TABLE " + tableName + " ADD data_hash_tokimnhj DATE");

            list.add("COMMENT ON COLUMN " + tableName + ".id_tokimnhj IS 'ID'");
            list.add("COMMENT ON COLUMN " + tableName + ".dir_id_tokimnhj IS '目录ID'");
            list.add("COMMENT ON COLUMN " + tableName + ".addtime_tokimnhj IS '增加时间'");
            list.add("COMMENT ON COLUMN " + tableName + ".updatetime_tokimnhj IS '更新时间'");
            list.add("COMMENT ON COLUMN " + tableName + ".state_tokimnhj IS '状态'");
            list.add("COMMENT ON COLUMN " + tableName + ".hash_tokimnhj IS '防窜改码'");
            list.add("COMMENT ON COLUMN " + tableName + ".data_hash_tokimnhj IS '防窜改码时间'");
        }

        System.out.println(" - - - 归集库|中心库 的 数据库表的 额外字段： SQL");
        list.stream().forEach(s -> System.out.println(s));
        return list;
    }

    /**
     * 判断表是否存在于数据库（报错则不存在）
     *
     * @param tableName 表名
     * @return
     */
    public static String isExistTableSql(String tableName) {
        return "SELECT COUNT (1) FROM " + tableName + " WHERE 1 = 2";
    }

    /**
     * 生成 从另一张表同步数据SQL
     *
     * @param databaseInfoVo           数据库连接信息
     * @param databaseType             数据库类型
     * @param lastTableName            旧版表名
     * @param newTableName             新版表名
     * @param lastColumnDefinitionList 旧版字段定义列表
     * @param newColumnDefinitionList  新版字段定义列表
     * @return
     * @throws APIException
     */
    public static String InsertDataFromTableSql(DatabaseInfoVo databaseInfoVo, Long databaseType, String lastTableName, String newTableName, List<String> lastColumnDefinitionList, List<String> newColumnDefinitionList) throws APIException {
        if (null == databaseType) {
            throw new APIException("databaseType 不可为空");
        }
        if (StringUtils.isBlank(lastTableName) || StringUtils.isBlank(newTableName)) {
            throw new APIException("表名不可为空");
        }
        if (null == databaseInfoVo) {
            throw new APIException("databaseInfo 不可为空");
        }
        StringBuffer sql = new StringBuffer("INSERT INTO  ");
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            sql.append(newTableName);
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            sql.append("\"" + databaseInfoVo.getSchema() + "\"." + newTableName)
                    .append("(")
                    .append(String.join(",", newColumnDefinitionList))
                    .append(") ")
                    .append("SELECT ")
                    .append(String.join(",", lastColumnDefinitionList))
                    .append(" FROM ")
                    .append("\"" + databaseInfoVo.getSchema() + "\".")
                    .append(lastTableName);
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            sql.append(newTableName)
                    .append("(")
                    .append(String.join(",", newColumnDefinitionList))
                    .append(") ")
                    .append("SELECT ")
                    .append(String.join(",", lastColumnDefinitionList))
                    .append(" FROM ")
                    .append(lastTableName);
        }
        System.out.println("———————————————— copy data from SQL ");
        System.out.println(sql);
        return sql.toString();
    }

    /**
     * 修改表名
     *
     * @param oldTableName
     * @param newTableName
     * @return
     * @throws APIException
     */
    public static String AlterTableName(String oldTableName, String newTableName) throws APIException {
        if (StringUtils.isBlank(oldTableName) || StringUtils.isBlank(newTableName)) {
            throw new APIException("tableName 不可为空");
        }
        String sql = "ALTER TABLE " + oldTableName + " RENAME TO " + newTableName;
        System.out.println("———————————————— alter table XXX rename to YYY ");
        System.out.println(sql);
        return sql;
    }
}
