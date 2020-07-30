package com.openjava.datalake.util;

import com.openjava.datalake.common.BsErrorCodeConstant;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.dataxjdbcutil.util.DbUtil;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.dto.TableOutputDTO;
import com.openjava.datalake.rescata.param.ColumnParam;
import com.openjava.datalake.rescata.vo.CountVo;
import com.openjava.datalake.rescata.vo.DataWithColumn;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author JiaHai
 * @Description 获取连接和释放资源的工具类
 */
@Log4j2
public class JdbcUtils {
    /**
     * HIVE的连接驱动
     */
    public static final String DRIVER_CLASS_HIVE = "org.apache.hive.jdbc.HiveDriver";
    /**
     * PostgreSQL的连接驱动
     */
//    public static final String DRIVER_CLASS_POSTGRESQL = "com.huawei.gauss200.jdbc.Driver";
    public static final String DRIVER_CLASS_POSTGRESQL = "org.postgresql.Driver";
    /**
     * MySQL的连接驱动
     */
    // 旧版mysql
    public static final String DRIVER_CLASS_MYSQL_OLD = "com.mysql.jdbc.Driver";
    // 新版mysql
    public static final String DRIVER_CLASS_MYSQL_NEW = "com.mysql.cj.jdbc.Driver";
    /**
     * Oracle的连接驱动
     */
    public static final String DRIVER_CLASS_ORACLE = "oracle.jdbc.OracleDriver";


    /**
     * HIVE的连接URL前缀
     */
    private static final String JDBC_URL_HIVE_PREFIX = "jdbc:hive2://";
    /**
     * PostgreSQL的连接URL前缀
     */
    private static final String JDBC_URL_POSTGRESQL_PREFIX = "jdbc:postgresql://";
    /**
     * MySql jdbcUrl前缀
     */
    private static final String JDBC_URL_MYSQL_PREFIX = "jdbc:mysql://";
    /**
     * Oracle的连接URL前缀
     */
    private static final String JDBC_URL_ORACLE_PREFIX = "jdbc:oracle:thin:@";

    /**
     * 用来设置“建立数据库连接的超时时间”
     */
    private static final int DRIVER_MANAGER_LOGIN_TIME_OUT = 5;

    /**
     * 生成JDBC URL
     *
     * @param databaseType
     * @param ip
     * @param port
     * @param databaseName
     * @param schema
     * @return
     */
    public static String generateJdbcUrl(Long databaseType, String ip, String port, String databaseName, String schema) {
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            // HIVE
            return JDBC_URL_HIVE_PREFIX + ip + ":" + port + "/" + databaseName;
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            // PostgreSQL
            return JDBC_URL_POSTGRESQL_PREFIX + ip + ":" + port + "/" + databaseName + "?currentSchema=" + schema;
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            // Oracle
            return JDBC_URL_ORACLE_PREFIX + ip + ":" + port + "/" + databaseName;
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            return JDBC_URL_MYSQL_PREFIX + ip + ":" + port + "/" + databaseName + "?character=UTF-8&serverTimezone=UTC";
        }
        return null;
    }

    /**
     * 生成带查询条件的统计SQL
     *
     * @param tableName
     * @param dlRescataColumnList
     * @param search
     * @param columnParamList
     * @param databaseType
     * @return
     * @throws APIException
     */
    public static String generateCountSql(String tableName, List<DlRescataColumn> dlRescataColumnList, String search, List<ColumnParam> columnParamList, Long databaseType) throws APIException {
        String sql = "SELECT COUNT(1) FROM " + tableName;
        sql += JdbcUtils.generateWhereSql(dlRescataColumnList, search, columnParamList, databaseType);
//        System.out.println("统计SQL：" + sql);
        if (log.isDebugEnabled()) {
            log.debug(sql);
        }
        return sql;
    }

    /**
     * 生成查询SQL
     *
     * @param tableName           表名
     * @param dlRescataColumnList 要查询的字段
     * @param search              全文搜索关键字
     * @param columnParamList     限制条件的字段（where条件）
     * @param page                起始页数
     * @param size                每页个数
     * @return
     * @throws APIException
     */
    public static String generateSql(String tableName, List<DlRescataColumn> dlRescataColumnList, String search, List<ColumnParam> columnParamList, int page, int size, Long databaseType) throws APIException {
        StringBuffer sql = new StringBuffer();
        // 获取“用于列表展示的 字段定义” （例如：XSXM）
        List<String> columnDefinitionList = dlRescataColumnList.stream().map(DlRescataColumn::getColumnDefinition).collect(Collectors.toList());

        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            // PostgreSQL
            sql.append("SELECT ").append(String.join(",", columnDefinitionList)).append(" FROM ").append(tableName);

            // 查询条件
            String whereSql = JdbcUtils.generateWhereSql(dlRescataColumnList, search, columnParamList, databaseType);
            if (StringUtils.isNotBlank(whereSql)) {
                sql.append(whereSql);
            }

            // 获取主键（主键排序，保证分页可以遍历所有数据）
            List<String> columnDefinitionPrimaryList = dlRescataColumnList.stream().filter(dlRescataColumn -> PublicConstant.YES.equals(dlRescataColumn.getIsPrimaryKey())).map(DlRescataColumn::getColumnDefinition).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(columnDefinitionPrimaryList)) {
                String primaryKey = columnDefinitionPrimaryList.get(0);
                sql.append(" ORDER BY ").append(primaryKey);
            }
            // 是否分页
            if (page >= 0 && size > 0) {
                sql.append(" LIMIT ").append(size).append(" OFFSET ").append(page * size);
            }
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            // Oracle
            if (page >= 0 && size > 0) {
                // 查询条件
                String whereSql = JdbcUtils.generateWhereSql(dlRescataColumnList, search, columnParamList, databaseType);
                if (StringUtils.isBlank(whereSql)) {
                    // 没有查询条件
                    whereSql = " WHERE ROWNUM < " + (page + 1) * size;
                } else {
                    whereSql = " AND ROWNUM < " + (page + 1) * size;
                }

                // 分页
                /*SELECT CS
                FROM (SELECT CS, ROWNUM AS RN
                        FROM O_LS_DGSZWFWSJGLJ_XZ_10042_V1
                        WHERE ROWNUM <= 5) TABLE_ALIAS
                WHERE TABLE_ALIAS.RN > 0*/
                // 子查询SQL
                StringBuffer nestedSql = new StringBuffer();
                nestedSql.append("SELECT ").append(String.join(",", columnDefinitionList)).append(", ROWNUM AS RN ").append(" FROM ").append(tableName).append(whereSql);

                sql.append("SELECT ").append(String.join(",", columnDefinitionList)).append(" FROM ( ")
                        .append(nestedSql)
                        .append(" ) TABLE_ALIAS WHERE TABLE_ALIAS.RN > ").append(page * size);

            } else {
                // 不分页
                sql.append("SELECT").append(String.join(",", columnDefinitionList)).append(" FROM ").append(tableName);

                // 查询条件
                String whereSql = JdbcUtils.generateWhereSql(dlRescataColumnList, search, columnParamList, databaseType);
                if (StringUtils.isNotBlank(whereSql)) {
                    sql.append(whereSql);
                }
            }
        }
        System.out.println("查询SQL：" + sql);

        return sql.toString();
    }

    /**
     * 生成Where条件SQL
     *
     * @param dlRescataColumnList
     * @param search
     * @param columnParamList
     * @param databaseType        数据库类型
     * @return
     * @throws APIException
     */
    public static String generateWhereSql(List<DlRescataColumn> dlRescataColumnList, String search, List<ColumnParam> columnParamList, Long databaseType) throws APIException {
        String whereSql = "";
        String dateFormat = "yyyy-MM-dd hh24:mi:ss";
        List<String> whereSqlList = new ArrayList<>();
        if (StringUtils.isNotBlank(search)) {
            // 全文模糊匹配
            if (CollectionUtils.isNotEmpty(dlRescataColumnList)) {
                if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
                    // TODO HIVE
                } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
                    // PostgreSQL
                    dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                        if (dlRescataColumn.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE) {
                            // 字符类型
                            whereSqlList.add(" " + dlRescataColumn.getColumnDefinition() + " LIKE '%" + search + "%' ");
                        } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dlRescataColumn.getDataType())) {
                            whereSqlList.add(" CAST (" + dlRescataColumn.getColumnDefinition() + " AS VARCHAR) LIKE '%" + search + "%' ");
                        } else {
                            whereSqlList.add(" CAST (" + dlRescataColumn.getColumnDefinition() + " AS VARCHAR) LIKE '%" + search + "%' ");
                        }
                    });
                } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
                    // Oracle
                    dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                        if (dlRescataColumn.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE) {
                            // 字符类型
                            whereSqlList.add(" " + dlRescataColumn.getColumnDefinition() + " LIKE '%" + search + "%' ");
                        } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dlRescataColumn.getDataType())) {
                            whereSqlList.add(" TO_CHAR (" + dlRescataColumn.getColumnDefinition() + ", '" + dateFormat + "') LIKE '%" + search + "%' ");
                        } else {
                            whereSqlList.add(" CAST (" + dlRescataColumn.getColumnDefinition() + " AS VARCHAR2(4000)) LIKE '%" + search + "%' ");
                        }
                    });
                }
                if (whereSqlList.size() != 0) {
                    whereSql += " WHERE " + String.join("OR", whereSqlList);
                }
            }
        } else if (CollectionUtils.isNotEmpty(columnParamList)) {
            // 按指定查询条件查询
            for (ColumnParam columnParam : columnParamList) {
                if (StringUtils.isNotBlank(columnParam.getColumnDefinition()) && StringUtils.isNotBlank(columnParam.getColumnValue())) {
                    if (columnParam.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE) {
                        // 字符类型
                        whereSqlList.add(" " + columnParam.getColumnDefinition() + " LIKE '%" + columnParam.getColumnValue() + "%' ");
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(columnParam.getDataType())) {
                        // 时间类型date
                        String columnValue = columnParam.getColumnValue();
                        String[] str = columnValue.split(",");
                        if (str.length == 1) {
                            // 只选择了“开始时间”
                            whereSqlList.add(" " + columnParam.getColumnDefinition() + " >= TO_DATE('" + str[0] + "', '" + dateFormat + "') ");
                        } else if (str.length == 2) {
                            if (StringUtils.isBlank(str[0])) {
                                // 只选择了“结束时间”
                                whereSqlList.add(" " + columnParam.getColumnDefinition() + " <= TO_DATE('" + str[1] + "', '" + dateFormat + "') ");
                            } else {
                                // 同时选择了“开始时间”和“结束时间”
                                whereSqlList.add(" " + columnParam.getColumnDefinition() + " BETWEEN TO_DATE('" + str[0] + "', '" + dateFormat + "') " + "AND TO_DATE('" + str[1] + "', '" + dateFormat + "') ");
                            }
                        }
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(columnParam.getDataType())) {
                        try {
                            // 整型（正则校验）
                            Long.parseLong(columnParam.getColumnValue());
                        } catch (NumberFormatException e) {
                            throw new APIException(APIConstants.CODE_PARAM_ERR, columnParam.getColumnDefinition() + " 需要为 纯数字");
                        }
                        whereSqlList.add(" " + columnParam.getColumnDefinition() + " = " + Long.valueOf(columnParam.getColumnValue()) + " ");
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(columnParam.getDataType())) {
                        // 浮点型（正则校验）
                        try {
                            // 浮点型
                            Double.parseDouble(columnParam.getColumnValue());
                        } catch (NumberFormatException e) {
                            throw new APIException(APIConstants.CODE_PARAM_ERR, columnParam.getColumnDefinition() + " 需要为 正数、负数或小数");
                        }
                        whereSqlList.add(" " + columnParam.getColumnDefinition() + " = " + Float.valueOf(columnParam.getColumnValue()) + " ");
                    }
                }
            }
            whereSql += " WHERE " + String.join("AND", whereSqlList);
        }
        return whereSql;
    }

    /**
     * 生成查询条件，仅用于PostgreSQL
     *
     * @param columnParamList
     * @return
     * @throws APIException
     */
    public static String generateWhereSql(List<ColumnParam> columnParamList) throws APIException {
        // 生成 查询条件SQL
        StringBuilder whereSql = new StringBuilder();
        if (CollectionUtils.isNotEmpty(columnParamList)) {
            String dateFormat = "yyyy-MM-dd hh24:mi:ss";
            List<String> columnParamSqlList = new ArrayList<>(columnParamList.size());
            for (ColumnParam columnParam : columnParamList) {
                // 获取 字段数据类型
                Long dataType = columnParam.getDataType();
                String columnDefinition = columnParam.getColumnDefinition();
                if (dataType < ResourceConstant.COLUMN_DATATYPE_DATE) {
                    // 字符类型
                    columnParamSqlList.add(columnDefinition + " LIKE ?");
                } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                    // 时间类型date
                    String columnValue = columnParam.getColumnValue();
                    String[] str = columnValue.split(",");
                    if (str.length == 1) {
                        // 只选择了“开始时间”
                        columnParamSqlList.add(columnDefinition + " >= TO_DATE(?, '" + dateFormat + "') ");
                    } else if (str.length == 2) {
                        if (StringUtils.isBlank(str[0])) {
                            // 只选择了“结束时间”
                            columnParamSqlList.add(columnDefinition + " <= TO_DATE(?, '" + dateFormat + "') ");
                        } else {
                            // 同时选择了“开始时间”和“结束时间”
                            columnParamSqlList.add(columnDefinition + " BETWEEN TO_DATE(?, '" + dateFormat + "') " + "AND TO_DATE(?, '" + dateFormat + "') ");
                        }
                    }
                } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                    try {
                        // 整型（正则校验）
                        Long.parseLong(columnParam.getColumnValue());
                    } catch (NumberFormatException e) {
                        throw new APIException(APIConstants.CODE_PARAM_ERR, columnDefinition + " 需要为 纯数字");
                    }
                    columnParamSqlList.add(columnDefinition + " = ?");
                } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                    try {
                        // 浮点型
                        Double.parseDouble(columnParam.getColumnValue());
                    } catch (NumberFormatException e) {
                        throw new APIException(APIConstants.CODE_PARAM_ERR, columnDefinition + " 需要为 正数、负数或小数");
                    }
                    columnParamSqlList.add(columnDefinition + " = ?");
                }
            }
            whereSql.append(" WHERE ").append(String.join(" AND ", columnParamSqlList));
        }
        return whereSql.toString();
    }

    /**
     * 生成统计表数据量的SQL （用于 目录数据管理）
     *
     * @param tableName
     * @param columnParamList
     * @return
     * @throws APIException
     */
    public static String generateCountSql(String tableName, List<ColumnParam> columnParamList) throws APIException {
        if (StringUtils.isBlank(tableName)) {
            throw new APIException(BsErrorCodeConstant.PUBLIC_COMMON_ERROR_CODE,"查询失败，表名不可为空");
        }

        // 生成 查询SQL
        StringBuilder selectSql = new StringBuilder();
        selectSql.append("SELECT COUNT(1) FROM ").append(tableName);

        // 生成 查询条件
        String whereSql = generateWhereSql(columnParamList);

        // 返回 完整查询SQL
        return selectSql.append(whereSql).toString();
    }

    /**
     * 生成查询表数据的SQL （用于 目录数据管理）
     *
     * @param tableName                  表名
     * @param selectColumnDefinitionList
     * @param columnParamList
     * @return
     * @throws APIException
     */
    public static String generateQuerySql(String tableName, List<String> selectColumnDefinitionList, List<ColumnParam> columnParamList, String primaryKeyColumnDefinition) throws APIException {
        if (StringUtils.isBlank(tableName)) {
            throw new APIException(BsErrorCodeConstant.PUBLIC_COMMON_ERROR_CODE,"查询失败，表名不可为空");
        }
        if (CollectionUtils.isEmpty(selectColumnDefinitionList)) {
            throw new APIException(BsErrorCodeConstant.PUBLIC_COMMON_ERROR_CODE,"查询失败，表字段不可为空");
        }

        // 生成 查询SQL
        StringBuilder selectSql = new StringBuilder();
        selectSql.append("SELECT ").append(String.join(",", selectColumnDefinitionList)).append(" FROM ").append(tableName);

        // 生成 查询条件
        String whereSql = generateWhereSql(columnParamList);

        // 生成 排序SQL
        // 主键不为空，则按主键排序
        String orderBySql = "";
        if (StringUtils.isNotBlank(primaryKeyColumnDefinition)) {
            orderBySql = " ORDER BY " + primaryKeyColumnDefinition;
        }

        // 生成 分页SQL
        StringBuilder pageSql = new StringBuilder(" LIMIT ? OFFSET ?");

        // 返回 完整查询SQL
        return selectSql.append(whereSql).append(orderBySql).append(pageSql).toString();
    }

    /**
     * 生成删除表中指定主键数据的SQL （用于 目录数据管理）
     *
     * @param tableName   表名
     * @param columnParam 参数
     * @return
     * @throws APIException
     */
    public static String generateDeleteSql(String tableName, ColumnParam columnParam) throws APIException {
        if (StringUtils.isBlank(tableName)) {
            throw new APIException(APIConstants.CODE_SERVER_ERR, "删除失败 表名不可为空");
        }
        // 生成 删除SQL
        StringBuilder deleteSql = new StringBuilder();
        deleteSql.append("DELETE FROM ").append(tableName);

        Long dataType = columnParam.getDataType();
        String columnDefinition = columnParam.getColumnDefinition();
        // 生成 查询条件
        String whereSql = " WHERE ";
        if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
            // 时间类型date
            String dateFormat = "yyyy-MM-dd hh24:mi:ss";
            whereSql += columnDefinition + " = TO_DATE(?, '" + dateFormat + "')";
        } else {
            // 非时间类型date
            if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                try {
                    // 整型（正则校验）
                    Long.parseLong(columnParam.getColumnValue());
                } catch (NumberFormatException e) {
                    throw new APIException(APIConstants.CODE_PARAM_ERR, columnDefinition + " 需要为 纯数字");
                }
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                try {
                    // 浮点型
                    Double.parseDouble(columnParam.getColumnValue());
                } catch (NumberFormatException e) {
                    throw new APIException(APIConstants.CODE_PARAM_ERR, columnDefinition + " 需要为 正数、负数或小数");
                }
            }
            whereSql += columnDefinition + " = ?";
        }
        return deleteSql.append(whereSql).toString();
    }

    /**
     * 生成删除表中指定主键数据的SQL （用于 目录数据管理）
     *
     * @param tableName             表名
     * @param columnParamList       参数
     * @param primaryKeyColumnParam 主键参数
     * @return
     * @throws APIException
     */
    public static String generateUpdateSql(String tableName, List<ColumnParam> columnParamList, ColumnParam primaryKeyColumnParam) throws APIException {
        if (StringUtils.isBlank(tableName)) {
            throw new APIException(APIConstants.CODE_SERVER_ERR, "修改失败 表名不可为空");
        }
        // 生成 删除SQL
        StringBuilder updateSql = new StringBuilder();
        updateSql.append("UPDATE ").append(tableName);

        // 生成 SET字段SQL
        String setColumnSql = generateSetColumnSql(columnParamList);

        // 生成 查询条件
        String whereSql = " WHERE " + primaryKeyColumnParam.getColumnDefinition() + " = ?";

        return updateSql.append(setColumnSql).append(whereSql).toString();
    }

    /**
     * 生成 SET字段SQL
     *
     * @param columnParamList
     * @return
     */
    public static String generateSetColumnSql(List<ColumnParam> columnParamList) {
        if (CollectionUtils.isEmpty(columnParamList)) {
            return null;
        }
        List<String> stringList = new ArrayList<>(columnParamList.size());
        columnParamList.stream().forEach(columnParam -> {
            // 字段定义
            String columnDefinition = columnParam.getColumnDefinition();
            // 添加到List中
            stringList.add(" " + columnDefinition + " = ?");
        });
        return " SET" + String.join(",", stringList);
    }

    /**
     * 预编译统计数据（用于 数据提供管理）
     *
     * @param sql             预编译前的SQL
     * @param columnParamList 参数列表
     * @param databaseInfoVo  连接信息
     * @param databaseType    数据库类型
     * @return 表头+数据
     * @throws Exception
     */
    public static Long generatePrepareCountSqlAndExecute(String sql, List<ColumnParam> columnParamList, DatabaseInfoVo databaseInfoVo, Long databaseType) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);
            // SQL参数赋值
            int i = 1;
            if (CollectionUtils.isNotEmpty(columnParamList)) {
                // 参数不为空
                for (ColumnParam columnParam : columnParamList) {
                    // 获取 字段数据类型
                    Long dataType = columnParam.getDataType();
                    String columnValue = columnParam.getColumnValue();
                    if (dataType < ResourceConstant.COLUMN_DATATYPE_DATE) {
                        // 字符类型
                        preparedStatement.setString(i, "%" + columnValue + "%");
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                        // 时间类型date
                        String[] str = columnValue.split(",");
                        if (str.length == 1) {
                            // 只选择了“开始时间”
                            preparedStatement.setString(i, str[0]);
                        } else if (str.length == 2) {
                            if (StringUtils.isBlank(str[0])) {
                                // 只选择了“结束时间”
                                preparedStatement.setString(i, str[1]);
                            } else {
                                // 同时选择了“开始时间”和“结束时间”
                                preparedStatement.setString(i++, str[0]);
                                preparedStatement.setString(i, str[1]);
                            }
                        }
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                        // 整型（正则校验）
                        preparedStatement.setLong(i, Long.valueOf(columnValue));
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                        // 浮点型
                        preparedStatement.setDouble(i, Double.valueOf(columnValue));
                    }
                    i++;
                }
            }

            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"统计SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return PublicConstant.NO;
    }

    /**
     * 预编译查询数据（用于 数据提供管理）
     *
     * @param sql                 预编译前的SQL
     * @param columnParamList     参数列表
     * @param page                页数（自然数）
     * @param size                每页条数（需要大于0）
     * @param databaseInfoVo      连接信息
     * @param databaseType        数据库类型
     * @param dlRescataColumnList select的字段信息
     * @return 表头+数据
     * @throws Exception
     */
    public static List<Object[]> generatePrepareQuerySqlAndExecute(String sql, List<ColumnParam> columnParamList, int page, int size, DatabaseInfoVo databaseInfoVo, Long databaseType, List<DlRescataColumn> dlRescataColumnList) throws Exception {
        List<Object[]> objectList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);
            // SQL参数赋值
            int i = 1;
            if (CollectionUtils.isNotEmpty(columnParamList)) {
                // 参数不为空
                for (ColumnParam columnParam : columnParamList) {
                    // 获取 字段数据类型
                    Long dataType = columnParam.getDataType();
                    String columnValue = columnParam.getColumnValue();
                    if (dataType < ResourceConstant.COLUMN_DATATYPE_DATE) {
                        // 字符类型
                        preparedStatement.setString(i, "%" + columnValue + "%");
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                        // 时间类型date
                        String[] str = columnValue.split(",");
                        if (str.length == 1) {
                            // 只选择了“开始时间”
                            preparedStatement.setString(i, str[0]);
                        } else if (str.length == 2) {
                            if (StringUtils.isBlank(str[0])) {
                                // 只选择了“结束时间”
                                preparedStatement.setString(i, str[1]);
                            } else {
                                // 同时选择了“开始时间”和“结束时间”
                                preparedStatement.setString(i++, str[0]);
                                preparedStatement.setString(i, str[1]);
                            }
                        }
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                        // 整型（正则校验）
                        preparedStatement.setLong(i, Long.valueOf(columnValue));
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                        // 浮点型
                        preparedStatement.setDouble(i, Double.valueOf(columnValue));
                    }
                    i++;
                }
            }
            preparedStatement.setInt(i++, size);
            preparedStatement.setInt(i, page * size);

            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 统计 字段总数
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Object[] object = new Object[columnCount];
                for (int j = 0; j < columnCount; j++) {
                    // （按顺序）获取数据类型
                    Long dataType = dlRescataColumnList.get(j).getDataType();

                    // 适配接收方式
                    if (ResourceConstant.COLUMN_DATATYPE_SHORT_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_LONG_STRING.equals(dataType)) {
                        object[j] = resultSet.getString(j + 1);
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                        object[j] = resultSet.getString(j + 1);
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                        String value = resultSet.getString(j + 1);
                        object[j] = null == value ? null : Long.valueOf(value);
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                        String str = resultSet.getString(j + 1);
                        if (null == str) {
                            // 数据为空
                            object[j] = "";
                        } else {
                            // 有数据
                            object[j] = new BigDecimal(resultSet.getDouble(j + 1) + "").toString();
                        }
                    }
                }
                objectList.add(object);
            }
            return objectList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"查询SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
    }

    /**
     * 预编译删除数据（用于 数据提供管理）
     *
     * @param sql            预编译前的SQL
     * @param columnParam    参数
     * @param databaseInfoVo 数据库连接信息
     * @param databaseType   数据库类型
     * @throws Exception
     */
    public static void prepareDeleteSqlAndExecute(String sql, ColumnParam columnParam, DatabaseInfoVo databaseInfoVo, Long databaseType) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);

            Long dataType = columnParam.getDataType();
            String columnValue = columnParam.getColumnValue();

            if (dataType < ResourceConstant.COLUMN_DATATYPE_DATE) {
                // 字符类型
                preparedStatement.setString(1, columnValue);
            } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                // 时间类型date
                preparedStatement.setString(1, columnValue);
            } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                // 整型（正则校验）
                preparedStatement.setLong(1, Long.valueOf(columnValue));
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                // 浮点型
                preparedStatement.setDouble(1, Double.valueOf(columnValue));
            }

            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                System.out.println("删除数据成功");
            } else {
                System.out.println("影响行数为0，删除数据失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, "删除SQL 执行有误");
        } finally {
            JdbcUtils.release(null, preparedStatement, connection);
        }
    }

    /**
     * 更新数据（用于 数据提供管理）
     *
     * @param sql                   预编译SQL
     * @param columnParamList       参数
     * @param primaryKeyColumnParam 主键对象参数
     * @param databaseInfoVo        数据库连接信息
     * @param databaseType          数据库类型
     * @throws Exception
     */
    public static void prepareUpdateSqlAndExecute(String sql, List<ColumnParam> columnParamList, ColumnParam primaryKeyColumnParam, DatabaseInfoVo databaseInfoVo, Long databaseType) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);

            if (CollectionUtils.isNotEmpty(columnParamList)) {
                // 参数不为空
                for (int i = 0, length = columnParamList.size(); i < length; i++) {
                    // 获取 字段数据类型
                    Long dataType = columnParamList.get(i).getDataType();
                    String columnValue = columnParamList.get(i).getColumnValue();
                    String columnDefinition = columnParamList.get(i).getColumnDefinition();
                    if (dataType < ResourceConstant.COLUMN_DATATYPE_DATE) {
                        // 字符类型
                        preparedStatement.setString(i + 1, columnValue);
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                        // 时间类型date
                        preparedStatement.setTimestamp(i + 1, new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(columnValue).getTime()));
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                        // 整型（正则校验）
                        try {
                            // 整型（正则校验）
                            Long.parseLong(columnValue);
                        } catch (NumberFormatException e) {
                            throw new APIException(APIConstants.CODE_PARAM_ERR, columnDefinition + " 需要为 纯数字");
                        }
                        preparedStatement.setLong(i + 1, Long.valueOf(columnValue));
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                        // 浮点型
                        try {
                            // 浮点型
                            Double.parseDouble(columnValue);
                        } catch (NumberFormatException e) {
                            throw new APIException(APIConstants.CODE_PARAM_ERR, columnDefinition + " 需要为 正数、负数或小数");
                        }
                        preparedStatement.setDouble(i + 1, Double.valueOf(columnValue));
                    }
                }
            }

            // where 语句 入参设置
            // 主键 数据类型
            Long dataType = primaryKeyColumnParam.getDataType();
            // 主键 值
            String columnValue = primaryKeyColumnParam.getColumnValue();
            int primaryKeyIndex = columnParamList.size() + 1;
            if (dataType < ResourceConstant.COLUMN_DATATYPE_DATE) {
                // 字符类型
                preparedStatement.setString(primaryKeyIndex, columnValue);
            } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                // 时间类型date
                preparedStatement.setTimestamp(primaryKeyIndex, new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(columnValue).getTime()));
            } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                // 整型（正则校验）
                preparedStatement.setLong(primaryKeyIndex, Long.valueOf(columnValue));
            } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                // 浮点型
                preparedStatement.setDouble(primaryKeyIndex, Double.valueOf(columnValue));
            }

            // 执行SQL
            int j = preparedStatement.executeUpdate();
            if (j > 0) {
                System.out.println("更新数据成功");
            } else {
                System.out.println("影响行数为0，更新数据失败");
            }
        } catch (APIException apiException) {
            throw new APIException(APIConstants.CODE_PARAM_ERR, apiException.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, "更新SQL 执行有误");
        } finally {
            JdbcUtils.release(null, preparedStatement, connection);
        }
    }

    /**
     * 分表统计 “资源记录总量” （物理数据库中，所有符合条件的表的记录数之和）（准实时）
     *
     * @param schema
     * @return
     */
    public static String generateSqlOfCountByTable(String schema) {
        // 声明SQL语句
        StringBuilder sql = new StringBuilder();
        // 1、统计符合条件的每张表的数据量； 2、求和
        sql.append("SELECT relname AS tableName, reltuples AS rowCounts FROM pg_class ")
                .append("WHERE relkind = 'r' ")
                .append("AND relnamespace = ")
                .append(String.format("(SELECT oid FROM pg_namespace WHERE nspname = '%s' ) ", schema))
                .append("AND relname !~ '_v[1-9]'");
        // 3、返回
        return sql.toString();
    }

    /**
     * 分表统计记录数量 （物理数据库中，所有符合条件的表的记录数之和）（准实时）
     *
     * @param schema
     * @return
     */
    public static String generateSqlForCountByTable(String schema) {
        // 声明SQL语句
        StringBuilder sql = new StringBuilder();
        // 1、统计符合条件的每张表的数据量； 2、求和
        sql.append("SELECT relname AS tableName, reltuples AS rowCounts FROM pg_class ")
                .append("WHERE relkind = 'r' ")
                .append("AND relnamespace = ")
                .append(String.format("(SELECT oid FROM pg_namespace WHERE nspname = '%s' ) ", schema));
        // 3、返回
        return sql.toString();
    }

    /**
     * 获取库的所有表名
     *
     * @param schema
     * @return
     */
    public static String generateSqlForTables(String schema) {
        return String.format("select tablename from pg_tables where schemaname = '%s' ", schema);
    }

    /**
     * 获取连接
     *
     * @param driverClass
     * @param url
     * @param user
     * @param password
     * @return
     * @throws APIException
     */
    public static Connection getConnection(String driverClass, String url, String user, String password) throws APIException {
        Connection connection;
        try {
            // 注册驱动
            Class.forName(driverClass);
            // 设置 建立数据库连接的超时时间
            DriverManager.setLoginTimeout(DRIVER_MANAGER_LOGIN_TIME_OUT);
//            connection = DriverManager.getConnection(url, user, password);
            // 改为用连接池
            connection = DbUtil.getConnByHikar(url, user, password, driverClass);
        } catch (ClassNotFoundException e) {
            System.err.println("驱动加载出错！");
            throw new APIException(APIConstants.CODE_SERVER_ERR,"驱动加载出错！");
        } catch (Exception e) {
            throw new APIException(APIConstants.CODE_SERVER_ERR,"建立数据库连接超时");
        }
        return connection;
    }

    /**
     * 根据数据库连接信息和数据库类型，获取连接
     *
     * @param databaseInfoVo
     * @param databaseType
     * @return
     */
    public static Connection getConnection(DatabaseInfoVo databaseInfoVo, Long databaseType) throws APIException {
        if (null == databaseInfoVo) {
            throw new APIException(BsErrorCodeConstant.PUBLIC_COMMON_ERROR_CODE,"数据库连接信息为空");
        }
        Connection connection;
        String ip = databaseInfoVo.getIp();
        String port = databaseInfoVo.getPort();
        String databaseName = databaseInfoVo.getDatabaseName();
        String schema = databaseInfoVo.getSchema();
        String jdbcUrl = databaseInfoVo.getJdbcUrl();
        if (StringUtils.isBlank(jdbcUrl)) {
            jdbcUrl = generateJdbcUrl(databaseType, ip, port, databaseName, schema);
        }
        String driverClass = JdbcUtils.getDriverByDatabaseType(databaseType);
        try {
            // 注册驱动
//            Class.forName(driverClass);
            // 设置 建立数据库连接的超时时间
            DriverManager.setLoginTimeout(DRIVER_MANAGER_LOGIN_TIME_OUT);
//            connection = DriverManager.getConnection(url, databaseInfoVo.getUsername(), databaseInfoVo.getPassword());
            // 改为用连接池
            connection = DbUtil.getConnByHikar(jdbcUrl, databaseInfoVo.getUsername(), databaseInfoVo.getPassword(), driverClass);
//        } catch (ClassNotFoundException e) {
//            System.err.println("驱动加载出错！");
//            throw new APIException("驱动加载出错！");
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, "服务繁忙，建立数据库连接超时，请稍后再试");
        }
        return connection;
    }

    /**
     * 释放资源
     *
     * @param resultSet
     * @param statement
     * @param connection
     */
    public static void release(ResultSet resultSet, Statement statement, Connection connection) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 创建、删除表，更新表数据
     *
     * @param driverClass
     * @param url
     * @param user
     * @param password
     * @param sql
     */
    public static void executeUpdate(String driverClass, String url, String user, String password, String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate(sql);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(null, preparedStatement, connection);
        }
    }

    /**
     * 创建、删除表，更新表数据
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sql
     */
    public static void executeUpdate(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            release(null, preparedStatement, connection);
        }
    }

    /**
     * 创建、删除表，更新表数据（批量SQL执行）
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sqlList
     * @throws APIException
     */
    public static void executeUpdate(DatabaseInfoVo databaseInfoVo, Long databaseType, List<String> sqlList) throws APIException {
        if (CollectionUtils.isEmpty(sqlList)) {
            return;
        }
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            statement = connection.createStatement();
            for (String sql : sqlList) {
                statement.executeUpdate(sql);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"SQL执行失败");
        } finally {
            release(null, statement, connection);
        }
    }

    /**
     * 自定义执行SQL
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param createTableSql
     * @param commentTabelSql
     * @param setPrimaryKeySql
     * @param commentColumnSqlList
     * @param columnDefaultSqlList
     * @param columnIndexSqlList
     * @throws Exception
     */
    public static void customExecuteUpdate(DatabaseInfoVo databaseInfoVo, Long databaseType, String createTableSql, String commentTabelSql, String setPrimaryKeySql, List<String> commentColumnSqlList, List<String> columnDefaultSqlList, List<String> columnIndexSqlList) throws Exception {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            statement = connection.createStatement();
            if (StringUtils.isNotBlank(createTableSql)) {

                int createStatus;
                for (int i = 0; i < 10; i++) {
                    createStatus = statement.executeUpdate(createTableSql);
                    if (createStatus != 0) {
                        // 执行失败
                        if (i > 2) {
                            Thread.sleep(3000);
                        }
                    } else {
                        // 执行成功
                        break;
                    }

                }

//                statement.executeUpdate(createTableSql);
            }
            if (StringUtils.isNotBlank(commentTabelSql)) {
                statement.executeUpdate(commentTabelSql);
            }
            if (StringUtils.isNotBlank(setPrimaryKeySql)) {
                statement.executeUpdate(setPrimaryKeySql);
            }
            if (CollectionUtils.isNotEmpty(commentColumnSqlList)) {
                for (String commentColumnSql : commentColumnSqlList) {
                    statement.executeUpdate(commentColumnSql);
                }
            }
            if (CollectionUtils.isNotEmpty(columnDefaultSqlList)) {
                for (String columnDefaultSql : columnDefaultSqlList) {
                    statement.executeUpdate(columnDefaultSql);
                }
            }
            if (CollectionUtils.isNotEmpty(columnIndexSqlList)) {
                for (String columnIndexSql : columnIndexSqlList) {
                    statement.executeUpdate(columnIndexSql);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, e.getMessage());
        } finally {
            release(null, statement, connection);
        }
    }

    /**
     * 数据库索引：创建、删除、查询（统计）
     *
     * @param databaseInfoVo 数据库连接信息
     * @param databaseType   数据库类型
     * @param IndexSql       SQL语句
     */
    public static int customExecuteIndex(DatabaseInfoVo databaseInfoVo, Long databaseType, String IndexSql) {
        Connection connection = null;
        Statement statement = null;
        int result = 0;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            statement = connection.createStatement();
            if (StringUtils.isNotBlank(IndexSql)) {
                result = statement.executeUpdate(IndexSql);
            }
        } catch (Exception e) {
            System.out.println("索引可能已经创建 或 索引不存在");
            e.printStackTrace();
        } finally {
            release(null, statement, connection);
        }
        return result;
    }

    /**
     * 查询数据，返回List<Map<String, Object>>
     *
     * @param driverClass
     * @param url
     * @param user
     * @param password
     * @param sql
     * @param columnList
     * @return
     */
    public static List<Map<String, Object>> executeQueryReturnMapList(String driverClass, String url, String user, String password, String sql, List<String> columnList) throws APIException {
        List<Map<String, Object>> mapList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getConnection(driverClass, url, user, password);
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<>();
                for (int i = 0, columnCount = columnList.size(); i < columnCount; i++) {
                    map.put(columnList.get(i), resultSet.getObject(columnList.get(i)));
                }
                mapList.add(map);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return mapList;
    }

    /**
     * 查询数据，返回List<Object[]>
     *
     * @param driverClass
     * @param url
     * @param user
     * @param password
     * @param sql
     * @param columnList
     * @return
     */
    public static List<Object[]> executeQuery(String driverClass, String url, String user, String password, String sql, List<String> columnList) throws APIException {
        List<Object[]> objectList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getConnection(driverClass, url, user, password);
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Object[] object = new Object[columnList.size()];
                for (int i = 0, columnCount = columnList.size(); i < columnCount; i++) {
                    object[i] = resultSet.getString(columnList.get(i));
                }
                objectList.add(object);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return objectList;
    }

    /**
     * 查询数据，返回 DataWithColumn
     *
     * @param databaseInfoVo
     * @param databaseType              数据库类型
     * @param sql
     * @param selectDlrescataColumnList
     * @return
     * @throws APIException
     */
    public static DataWithColumn executeQuery(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql, List<DlRescataColumn> selectDlrescataColumnList) throws APIException {
        List<String> columnNameList = new ArrayList<>();
        List<Object[]> objectList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
//            if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
//                HiveUtils hiveUtils = new HiveUtils();
//                connection = hiveUtils.getConn();
//            } else {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
//            }

            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 统计 字段总数
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Object[] object = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    // （按顺序）获取数据类型
                    Long dataType = selectDlrescataColumnList.get(i).getDataType();

                    // 适配接收方式
                    if (ResourceConstant.COLUMN_DATATYPE_SHORT_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_MIDDLE_STRING.equals(dataType) || ResourceConstant.COLUMN_DATATYPE_LONG_STRING.equals(dataType)) {
                        object[i] = resultSet.getString(i + 1);
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(dataType)) {
                        object[i] = resultSet.getString(i + 1);
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(dataType)) {
                        String value = resultSet.getString(i + 1);
                        object[i] = null == value ? null : Long.valueOf(value);
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(dataType)) {
                        object[i] = new BigDecimal(resultSet.getDouble(i + 1) + "").toString();
                    }
                }
                objectList.add(object);
            }
            for (int i = 0; i < columnCount; i++) {
                columnNameList.add(resultSetMetaData.getColumnName(i + 1).toLowerCase());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"查询SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return new DataWithColumn(columnNameList, objectList);
    }

    /**
     * 根据表名，统计该表的数据量
     *
     * @param databaseInfoVo 数据库连接信息
     * @param databaseType   数据库类型
     * @param sql            统计SQL
     * @return
     * @throws APIException
     */
    public static Long executeCount(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
//            if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
//                HiveUtils hiveUtils = new HiveUtils();
//                connection = hiveUtils.getConn();
//            } else {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
//            }

            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"统计SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return null;
    }

    /**
     * 根据表名，统计该表的数据量（非准确值）
     *
     * @param databaseInfoVo 数据库连接信息
     * @param databaseType   数据库类型
     * @param sql            统计SQL
     * @return
     * @throws APIException
     */
    public static List<CountVo> countByTable(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<CountVo> countVoList = new ArrayList<>();
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                CountVo countVo = new CountVo(resultSet.getString(1), resultSet.getLong(2));
                countVoList.add(countVo);
            }
            return countVoList;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"统计SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
    }

    /**
     * 根据表名，统计该表的数据量（非准确值）
     *
     * @param databaseInfoVo 数据库连接信息
     * @return
     * @throws APIException
     */
    public static List<CountVo> countByTable(DatabaseInfoVo databaseInfoVo) throws APIException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String countSql = "select count(1) from  ";

        List<CountVo> countVoList = new ArrayList<>();
        List<String> tableList = new ArrayList<>();
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseInfoVo.getDatabaseType());

            //查询所有的表
            String sqlForTables = generateSqlForTables(databaseInfoVo.getSchema());
            // 预编译
            preparedStatement = connection.prepareStatement(sqlForTables);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                tableList.add(resultSet.getString(1));
            }

            //根据表名查询记录数
            for (String tableName : tableList) {
                // 预编译
                preparedStatement = connection.prepareStatement(countSql + tableName);
                // 执行查询
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    CountVo countVo = new CountVo(tableName, resultSet.getLong(1));
                    countVoList.add(countVo);
                }
            }
            return countVoList;
        } catch (SQLException e) {
            //e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"统计SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
    }

    /**
     * 根据表名，统计该表的数据量
     *
     * @param sql 统计SQL
     * @return
     * @throws APIException
     */
    public static Long executeCount(String driverClass, String url, String user, String password, String sql) throws APIException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
//            if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
//                HiveUtils hiveUtils = new HiveUtils();
//                connection = hiveUtils.getConn();
//            } else {
            Class.forName(driverClass);
            connection = DriverManager.getConnection(url, user, password);
//            }

            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR,"统计SQL 执行有误");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return null;
    }

    public static List<String> getSelectColumnAlias(String sql) {
        String SQL_SELECT = "(?:select|SELECT).*?(?<=from|FROM)";
        Pattern pattern = Pattern.compile(SQL_SELECT);
        Matcher matcher = pattern.matcher(sql);
        boolean b = matcher.find();
        if (b) {
            String group = matcher.group();
            String s = group.replaceAll("(select|SELECT|FROM|from|\\s)", "");
            String[] split = s.split(",");
            return new ArrayList<>(Arrays.asList(split));
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 根据数据库类型，获取数据库连接驱动
     *
     * @param databaseType
     * @return
     */
    public static String getDriverByDatabaseType(Long databaseType) {
        if (databaseType == null) {
            return null;
        }
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            return DRIVER_CLASS_HIVE;
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            return DRIVER_CLASS_POSTGRESQL;
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            return DRIVER_CLASS_ORACLE;
        } else if (ResourceConstant.DATABASE_TYPE_MYSQL.equals(databaseType)) {
            return DRIVER_CLASS_MYSQL_NEW;
        }
        return null;
    }

    /**
     * 获取指定数据库的所有表信息（表名 + 表注释）
     *
     * @param databaseInfoVo
     * @param databaseType   数据库类型
     */
    public static List<TableOutputDTO> getTableList(DatabaseInfoVo databaseInfoVo, Long databaseType, String tableNameLike) {
        Connection connection = null;
        ResultSet resultSet = null;

        // 初始化返回对象
        List<TableOutputDTO> tableOutputDTOList = new ArrayList<>();

        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            DatabaseMetaData databaseMetaData = connection.getMetaData();

            // 暂且只返回类型：TABLE+VIEW（全部表类型。典型的类型是 "TABLE"、"VIEW"、"SYSTEM TABLE"、"GLOBAL TEMPORARY"、"LOCAL TEMPORARY"、"ALIAS" 和 "SYNONYM"。）
            resultSet = databaseMetaData.getTables(connection.getCatalog(), connection.getSchema(), tableNameLike, new String[]{"TABLE", "VIEW"});
            while (resultSet.next()) {
                TableOutputDTO tableOutputDTO = new TableOutputDTO(resultSet.getString("TABLE_NAME"), resultSet.getString("REMARKS"));
                tableOutputDTOList.add(tableOutputDTO);
            }
            return tableOutputDTOList;
        } catch (Exception e) {
            e.printStackTrace();
            return tableOutputDTOList;
        } finally {
            JdbcUtils.release(resultSet, null, connection);
        }
    }

    /**
     * 获取指定数据库的指定表的所有字段信息
     *
     * @param databaseInfoVo
     * @param databaseType   数据库类型
     * @param tableName      表名
     * @return
     */
    public static List<DlRescataColumn> getColumnList(DatabaseInfoVo databaseInfoVo, Long databaseType, String tableName) {
        Connection connection = null;
        ResultSet resultSet = null;

        // 初始化返回对象
        List<DlRescataColumn> dlRescataColumnList = new ArrayList<>();
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            DatabaseMetaData metaData = connection.getMetaData();
            // 精准匹配表，获取主键信息
            ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), connection.getSchema(), tableName);
            String primaryKeyName = null;
            if (primaryKeys.next()) {
                primaryKeyName = primaryKeys.getString("COLUMN_NAME");
            }

            //精准匹配表，获取表字段信息q
            resultSet = metaData.getColumns(connection.getCatalog(), connection.getSchema(), tableName, null);
            while (resultSet.next()) {
                // 字段名
                String columnName = resultSet.getString("COLUMN_NAME");
                // 字段类型
                String columnType = resultSet.getString("TYPE_NAME");
                // 字段长度
                int columnLength = resultSet.getInt("COLUMN_SIZE");
                // 字段注释
                String remarks = resultSet.getString("REMARKS");
                // 是否为空：0表示Not Null，1表示可以Null
                int nullable = resultSet.getInt("NULLABLE");

                DlRescataColumn dlRescataColumn = new DlRescataColumn();
                dlRescataColumn.setColumnComment(remarks)
                        // 字段注释
                        .setInfoDescription(remarks)
                        // 字段名称
                        .setColumnName(remarks)
                        // 字段类型
                        .setDataType(ColumnTypeUtils.convertToDataLakeColumnType(columnType))
                        // 字段长度
                        .setColumnLength((long) columnLength)
                        // 字段定义
                        .setColumnDefinition(columnName.toLowerCase())
                        // 是否必填
                        .setIsRequire(nullable == 0 ? PublicConstant.YES : PublicConstant.NO)
                        // 是否主键
                        .setIsPrimaryKey(Objects.equals(primaryKeyName, columnName) ? PublicConstant.YES : PublicConstant.NO);
                dlRescataColumnList.add(dlRescataColumn);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, null, connection);
        }
        return dlRescataColumnList;
    }

    public static void main(String[] args) throws APIException {
        // PostgreSQL
        String sql = "select user_id, user_name, user_score from t_users";


        DatabaseInfoVo databaseInfoVo = new DatabaseInfoVo("219.135.182.2", "54321", "wjh", "public", "mppuser", "ioc123456", 2L, null);

        // Oracle
//        String sql = "SELECT T.USER_ID, T.USER_NAME, T.USER_BIRTH, T.USER_SALARY FROM A_B T";
//        DatabaseInfoVo databaseInfoVo = new DatabaseInfoVo("nhc.smart-info.cn", "8521", "", "orcl", "c##dgioc", "bigdata@0769", 3L, null);

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, 2L);
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String string = resultSet.getString(3);
                if (null == string) {
                    string = "";
                }
                System.out.println(resultSet.getString(1) + " || " + resultSet.getString(2) + " || " + new BigDecimal(resultSet.getDouble(3) + "").toString());
                System.out.println(resultSet.getString(1) + " || " + resultSet.getString(2) + " || " + string);
                System.out.println();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
    }

    /**
     * 创建、删除表，更新表数据
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sql
     */
    public static void executeUpdateThrowError(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.executeUpdate();
        } finally {
            release(null, preparedStatement, connection);
        }
    }

    /**
     * 使用事务批量新增和更新表数据
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sqls
     */
    public static void executeUpdateThrowError(DatabaseInfoVo databaseInfoVo, Long databaseType, List<String> sqls) throws APIException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            connection.setAutoCommit(false);
            for (String sql : sqls) {
                logger.info(sql);
                preparedStatement = connection.prepareStatement(sql);
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (Exception ex) {
            connection.rollback();
            if(ex.getMessage().contains("unique")){
                throw new APIException(400, "写入数据失败，请仔细检查您的数据主键是否重复！");
            }else {
                throw new APIException(400, "写入数据失败，请仔细检查您的数据是否有误！");
            }

        } finally {
            release(null, preparedStatement, connection);
        }
    }

    /**
     * 查询数据 谢沛辰
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sql
     */
    public static List<List<Object>> executeThrowError(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException, SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        Logger logger = LoggerFactory.getLogger(JdbcUtils.class);
        List<List<Object>> results = new ArrayList<>();
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            preparedStatement = connection.prepareStatement(sql);
            logger.info(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Object> title = new ArrayList<>();
            List<Object> row;
            Integer colCount = metaData.getColumnCount();
            for (Integer i = 1; i <= colCount; i++) {
                title.add(metaData.getColumnName(i));
            }
            results.add(title);
            while (resultSet.next()) {
                row = new ArrayList<>();
                for (Integer i = 1; i <= colCount; i++) {
                    row.add(resultSet.getObject(i));
                }
                results.add(row);
            }
        } finally {
            release(null, preparedStatement, connection);
        }

        return results;
    }

    /**
     * 查询数据行中的第一列数据
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sql
     */
    public static Object excuteScale(DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException, SQLException {
        List<List<Object>> results = executeThrowError(databaseInfoVo, databaseType, sql);
        if (results.size() > 1) {
            return results.get(1).get(0);
        }
        return null;
    }

    /**
     * 自定义执行SQL
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param createTableSql
     * @param commentTabelSql
     * @param setPrimaryKeySql
     * @param commentColumnSqlList
     * @param columnDefaultSqlList
     * @param columnIndexSqlList
     */
    public static void customExecuteUpdateThrowError(DatabaseInfoVo databaseInfoVo, Long databaseType, String createTableSql, String commentTabelSql, String setPrimaryKeySql, List<String> commentColumnSqlList, List<String> columnDefaultSqlList, List<String> columnIndexSqlList) throws APIException {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            statement = connection.createStatement();
            if (StringUtils.isNotBlank(createTableSql)) {
                statement.executeUpdate(createTableSql);
            }
            if (StringUtils.isNotBlank(commentTabelSql)) {
                statement.executeUpdate(commentTabelSql);
            }
            if (StringUtils.isNotBlank(setPrimaryKeySql)) {
                statement.executeUpdate(setPrimaryKeySql);
            }
            if (CollectionUtils.isNotEmpty(commentColumnSqlList)) {
                for (String commentColumnSql : commentColumnSqlList) {
                    statement.executeUpdate(commentColumnSql);
                }
            }
            if (CollectionUtils.isNotEmpty(columnDefaultSqlList)) {
                for (String columnDefaultSql : columnDefaultSqlList) {
                    statement.executeUpdate(columnDefaultSql);
                }
            }
            if (CollectionUtils.isNotEmpty(columnIndexSqlList)) {
                for (String columnIndexSql : columnIndexSqlList) {
                    statement.executeUpdate(columnIndexSql);
                }
            }
        } catch (APIException ex) {
            ex.printStackTrace();
            throw new APIException(400, ex.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(400, "创建表失败！");
        } finally {
            release(null, statement, connection);
        }
    }
}
