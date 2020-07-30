package com.openjava.datalake.util;

import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import org.apache.commons.lang3.StringUtils;

/**
 * @author: 张剑锋
 * @Date: 2019-9-10 14:03:15
 */
public class ColumnTypeUtils {

    /**
     * 短字符
     */
    private static final String DL_TYPE_SHORT_VAR = "1";
    /**
     * 较长字符
     */
    private static final String DL_TYPE_NORMAL_VAR = "2";
    /**
     * 长字符
     */
    private static final String DL_TYPE_LONG_VAR = "3";
    /**
     * 日期型
     */
    private static final String DL_TYPE_DATE = "4";
    /**
     * 整数型
     */
    private static final String DL_TYPE_INT = "5";
    /**
     * 小数型
     */
    private static final String DL_TYPE_DOUBLE = "6";

    /**
     * 转换数据湖字段类型
     *
     * @param dlColumnType 数据湖字段类型
     * @return 对应的类型：string number date
     */
    public static String dealDataLakeType(String dlColumnType) {
        if (StringUtils.isBlank(dlColumnType)) {
            return PublicConstant.COLUMN_TYPE_STRING;
        }

        if (StringUtils.equals(dlColumnType, DL_TYPE_SHORT_VAR) ||
                StringUtils.equals(dlColumnType, DL_TYPE_NORMAL_VAR) ||
                StringUtils.equals(dlColumnType, DL_TYPE_LONG_VAR)) {
            return PublicConstant.COLUMN_TYPE_STRING;
        }

        if (StringUtils.equals(dlColumnType, DL_TYPE_DATE)) {
            return PublicConstant.COLUMN_TYPE_DATE;
        }

        if (StringUtils.equals(dlColumnType, DL_TYPE_INT) ||
                StringUtils.equals(dlColumnType, DL_TYPE_DOUBLE)) {
            return PublicConstant.COLUMN_TYPE_NUMBER;
        }

        return PublicConstant.COLUMN_TYPE_STRING;
    }

    /**
     * 转换为数据湖宏观字段类型（对接汇聚接口）
     *
     * @param columnType
     * @return
     */
    public static Long convertQueryApiColumnTypeToDataLakeColumnType(String columnType) {
        if (StringUtils.isBlank(columnType)) {
            return null;
        }
        switch (columnType) {
            case PublicConstant.COLUMN_TYPE_STRING:
                return ResourceConstant.COLUMN_DATATYPE_LONG_STRING;
            case PublicConstant.COLUMN_TYPE_NUMBER:
                return ResourceConstant.COLUMN_DATATYPE_DECIMAL;
            case PublicConstant.COLUMN_TYPE_DATE:
                return ResourceConstant.COLUMN_DATATYPE_DATE;
        }
        return null;
    }
    /**
     * 转换为数据湖宏观字段类型（对接汇聚接口）
     *
     * @param columnType
     * @return
     */
    public static Long convertToDataLakeColumnType(String columnType) {
        if (StringUtils.isBlank(columnType)) {
            return null;
        }
        switch (columnType.toUpperCase()) {
            case "CHAR":
            case "NCHAR":
            case "VARCHAR":
            case "LONGVARCHAR":
            case "NVARCHAR":
            case "LONGNVARCHAR":
                return ResourceConstant.COLUMN_DATATYPE_LONG_STRING;
            case "CLOB":
            case "NCLOB":
            case "BINARY":
            case "VARBINARY":
            case "BLOB":
            case "LONGVARBINARY":
            case "BOOLEAN":
            case "BIT":
            case "NULL":
            case "TIME_WITH_TIMEZONE":
            case "TIMESTAMP_WITH_TIMEZONE":
                // TODO 临时方案 不可一一对应转换
                return ResourceConstant.COLUMN_DATATYPE_LONG_STRING;
//                break;
            case "SMALLINT":
            case "TINYINT":
            case "INTEGER":
            case "BIGINT":
            case "INT2":
            case "INT4":
            case "INT8":
                return ResourceConstant.COLUMN_DATATYPE_INTEGER;
            case "NUMERIC":
            case "DECIMAL":
            case "FLOAT":
            case "FLOAT4":
            case "FLOAT8":
            case "REAL":
            case "DOUBLE":
                return ResourceConstant.COLUMN_DATATYPE_DECIMAL;
            case "TIME":
            case "DATE":
            case "TIMESTAMP":
                return ResourceConstant.COLUMN_DATATYPE_DATE;
        }
        return null;
    }

    /**
     * 字段类型 数据字典转换
     *
     * @param dataTypeString
     * @return
     */
    public static Long convertToDataLakeColumnTypeFromChinese(String dataTypeString) {
        switch (dataTypeString) {
            case "短字符":
                return ResourceConstant.COLUMN_DATATYPE_SHORT_STRING;
            case "较长字符":
                return ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING;
            case "长字符":
                return ResourceConstant.COLUMN_DATATYPE_LONG_STRING;
            case "日期型":
                return ResourceConstant.COLUMN_DATATYPE_DATE;
            case "整数型":
                return ResourceConstant.COLUMN_DATATYPE_INTEGER;
            case "小数型":
                return ResourceConstant.COLUMN_DATATYPE_DECIMAL;
            default:
                return null;
        }
    }

    /**
     * 字段类型 转换
     *
     * @param databaseType 数据库类型（1HIVE、2MPP）
     * @param dataType     字段数据类型
     * @return
     */
    public static String convertToDataLakeColumnTypeFromNumber(Long databaseType, Long dataType) {
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            // HIVE
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            // MPP
            if (ResourceConstant.COLUMN_DATATYPE_SHORT_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_LONG_STRING.equals(dataType)) {
                // 字符串
                return DdlSqlUtils.POSTGRESQL_DATATYPE_STRING;
            } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                // 时间
                return DdlSqlUtils.POSTGRESQL_DATATYPE_DATE;
            } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                // 整数
                return DdlSqlUtils.POSTGRESQL_DATATYPE_INTEGER;
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                // 小数
                return DdlSqlUtils.POSTGRESQL_DATATYPE_DECIMAL;
            }
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            // Oracle
        }
        return "String";
    }
}
