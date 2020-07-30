package com.openjava.datalake.util;

import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.vo.DataWithColumn;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import com.openjava.datalake.smartapi.vo.ColumnParam;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author xjd
 * @Description 获取连接和释放资源的工具类
 */
public class JdbcApiQueryUtils {
    private static Logger logger = LoggerFactory.getLogger(JdbcApiQueryUtils.class);

    private static final String PERCENT_CHAR = "%";

    /**
     * 生成统计SQL
     *
     * @param tableName
     * @param dlRescataColumnList
     * @param columnParamList
     * @return selfConditionSql 自定义条件
     * @throws APIException
     */
    public static String generateCountSql(String tableName, List<DlRescataColumn> dlRescataColumnList, Long databaseType, List<ColumnParam> columnParamList,String selfConditionSql) throws APIException {
        String sql = "SELECT COUNT(1) FROM \"" + tableName + "\" ";
        sql += generateWhereSqlBeta(dlRescataColumnList, "", databaseType, columnParamList);
//        System.out.println("统计SQL：" + sql);
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
        if (StringUtils.isNotBlank(selfConditionSql)){
            sql += " "+ selfConditionSql+" ";
        }
        System.out.println(sql);
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
    public static String generateSql(String tableName, List<DlRescataColumn> dlRescataColumnList, String search, List<ColumnParam> columnParamList, Long databaseType, int page, int size,String selfConditionSql) throws APIException {
        //1.生成前半部分sql
        StringBuffer sqlSb = new StringBuffer();
        String selectSql = JdbcApiQueryUtils.generateSelectSql(tableName, dlRescataColumnList, databaseType);
        sqlSb.append(selectSql);
        //2.生成后半部分sql（where条件）
        String whereSql = JdbcApiQueryUtils.generateWhereSqlBeta(dlRescataColumnList, search, databaseType, columnParamList);
        if (StringUtils.isNotBlank(selfConditionSql)){
            whereSql += " "+selfConditionSql +" ";
        }
        if (StringUtils.isNotBlank(whereSql)) {
            sqlSb.append(whereSql);
        }

        if (CollectionUtils.isNotEmpty(dlRescataColumnList)){
            DlRescataColumn  column= dlRescataColumnList.get(0);
            //todo 加上order by
            String pKey = column.getColumnDefinition();
            sqlSb.append(" order by "+ pKey+" ");
        }

        //3.是否分页
        if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)
                || ResourceConstant.DATABASE_TYPE_MYSQL.equals(databaseType)) {
            // 是否分页
            if (page >= 0 && size > 0) {
                sqlSb.append(" LIMIT ").append(size).append(" OFFSET ").append(page * size);
            }
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            // Oracle
            // oracle 分页参数
            String pageHead = " select * from (select a.*, rownum rn from ( ";
            String pageTial =
                    " )a where " +
                            " rownum <= " +
                            (page + 1) * size +
                            " ) where " +
                            " rn > " +
                            page * size ;

            if (page >= 0 && size > 0) {
                // 加入oracle的头和尾
                sqlSb.insert(0, pageHead);
                sqlSb.append(pageTial);
            }
        }
        String sql = sqlSb.toString();
        if (logger.isDebugEnabled()) {
            logger.debug(sql);
        }
 //        System.out.println("查询SQL：" + sql);
        return sql;
    }

    /**
     * 生成where条件SQL
     *
     * @param dlRescataColumnList 改资源目录的所有信息项，用于匹配全文搜索
     * @param search              全文搜索的关键字
     * @param columnParamList     where 参数 && 参数值
     * @return where sql
     * @throws APIException
     * @author lwx
     */
    public static String generateWhereSqlBeta(List<DlRescataColumn> dlRescataColumnList, String search, Long databaseType, List<ColumnParam> columnParamList) throws APIException {
        String whereSql = "";
        String postgresqlDateFormat = "yyyy-MM-dd hh24:mi:ss";
        String oracleDateFormat = "yyyy-MM-dd hh24:mi:ss";
        List<String> whereSqlList = new ArrayList<>();
        if (StringUtils.isNotBlank(search)) {
            // 全文模糊匹配
            if (CollectionUtils.isNotEmpty(dlRescataColumnList)) {
                for (DlRescataColumn dlRescataColumn : dlRescataColumnList) {
                    if (dlRescataColumn.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE) {
                        // 字符类型
                        whereSqlList.add(" " + dlRescataColumn.getColumnDefinition() + " LIKE ?");
                    }
                }
                if (whereSqlList.size() != 0) {
                    whereSql += " WHERE " + String.join("OR", whereSqlList);
                }
            }
        } else if (CollectionUtils.isNotEmpty(columnParamList)) {
            List<ColumnParam> columnParamListCopy = null;
            if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
                columnParamListCopy = new ArrayList<>();
                for (ColumnParam columnParam : columnParamList) {
                    ColumnParam clone = columnParam.clone();
                    clone.setColumnDefinition("\"" + columnParam.getColumnDefinition() + "\"");
                    columnParamListCopy.add(clone);
                }
            } else {
                columnParamListCopy = columnParamList;
            }
            // 按指定查询条件查询
            for (ColumnParam columnParam : columnParamListCopy) {
                if (StringUtils.isNotBlank(columnParam.getColumnDefinition()) && StringUtils.isNotBlank(columnParam.getColumnValue())) {
                    if (columnParam.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE) {
                        // 字符类型
                        if (columnParam.getFuzzySearch()) {
                            if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
                                whereSqlList.add(" CAST (" + columnParam.getColumnDefinition() + " AS VARCHAR) LIKE ? ");
                            } else {
                                whereSqlList.add("" + columnParam.getColumnDefinition() + " LIKE ? ");
                            }
                        } else {
                            whereSqlList.add(" " + columnParam.getColumnDefinition() + " = ? ");
                        }
                    } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(columnParam.getDataType())) {
                        // 时间类型date
                        String columnValue = columnParam.getColumnValue();
                        String[] str = columnValue.split(",");
                        if (str.length == 1) {
                            // 只选择了“开始时间”
                            whereSqlList.add(" " + columnParam.getColumnDefinition() + " >= TO_DATE(?, '" + postgresqlDateFormat + "') ");
                        } else if (str.length == 2) {
                            if (StringUtils.isBlank(str[0])) {
                                // 只选择了“结束时间”
                                whereSqlList.add(" " + columnParam.getColumnDefinition() + " >= TO_DATE(?, '" + postgresqlDateFormat + "') ");
                            } else {
                                // 同时选择了“开始时间”和“结束时间”
                                whereSqlList.add(" " + columnParam.getColumnDefinition() + " BETWEEN TO_DATE(?, '" + postgresqlDateFormat + "') " + "AND TO_DATE(?, '" + postgresqlDateFormat + "') ");
                            }
                        }
                    } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(columnParam.getDataType())) {
                        // 整型（正则校验）
                        if (!Pattern.matches("^[0-9]*$", columnParam.getColumnValue())) {
                            throw new APIException(columnParam.getColumnDefinition() + " 需要为 纯数字");
                        }
                        whereSqlList.add(" " + columnParam.getColumnDefinition() + " = ? ");
                    } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(columnParam.getDataType())) {
                        // 浮点型（正则校验）
                        if (!Pattern.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$", columnParam.getColumnValue())) {
                            throw new APIException(columnParam.getColumnDefinition() + " 需要为 正数、负数或小数");
                        }
                        whereSqlList.add(" " + columnParam.getColumnDefinition() + " = ? ");
                    }
                }
            }
            if (whereSqlList.size() > 0) {
                whereSql += " WHERE " + String.join("AND", whereSqlList);
            }
        }
        return whereSql;
    }


    private static String generateSelectSql(String tableName, List<DlRescataColumn> selectParamList, Long databaseType) {
        // 获取“用于列表展示的 字段定义” （例如：XSXM）
        List<String> selectParam = selectParamList.stream().map(DlRescataColumn::getColumnDefinition).collect(Collectors.toList());
        // todo 识别源表是否用小写字段
        boolean isLowerTable = true;
        // 适配源表字段用小写的
        if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType) && isLowerTable) {
            for (int i = 0; i < selectParam.size(); i++) {
                String s = selectParam.get(i);
                s = "\"" + s + "\"";
                selectParam.set(i, s);
            }
        }
        // 拼接查询参数
        return "SELECT " + String.join(",", selectParam) + " FROM \"" + tableName + "\" ";
    }

    private static String generateInsertSql(String tableName, List<ColumnParam> insertParamList) {
        String sql = "INSERT INTO " + tableName + " ";
        List<String> paramList = new ArrayList<>(insertParamList.size());
        for (ColumnParam columnParam : insertParamList) {
            paramList.add(columnParam.getColumnDefinition());
        }
        sql += sql + "(" + String.join(",", paramList) + ") VALUES ";
        return sql;
    }

    /**
     * 填充PreparedStatement参数值
     *
     * @param dlRescataColumnList 改资源目录的所有信息项，用于匹配全文搜索
     * @param search              全文搜索的关键字
     * @param columnParamList     where 参数 && 参数值
     * @param preparedStatement   预编译对象
     * @return
     * @throws APIException
     * @author lwx
     */
    public static PreparedStatement fillData(List<DlRescataColumn> dlRescataColumnList, String search, List<ColumnParam> columnParamList, PreparedStatement preparedStatement) throws APIException {
        if (StringUtils.isNotBlank(search)) {
            // 全文模糊匹配
            if (CollectionUtils.isNotEmpty(dlRescataColumnList)) {
                for (int i = 0, len = dlRescataColumnList.size(); i < len; i++) {
                    try {
                        //preparedStatement下标是从1开始的，所以i+1
                        preparedStatement.setString(i + 1, genLikeWord(search));
                    } catch (SQLException e) {
                        System.out.println("【全局查询】preparedStatement设置参数值失败");
                        e.printStackTrace();
                    }
                }
            }
        } else if (CollectionUtils.isNotEmpty(columnParamList)) {
            // 按指定查询条件查询
            // 预编译后值的起始位置
            int startIndex;
            if (StringUtils.isNotBlank(search)) {
                startIndex = dlRescataColumnList.size();
            } else {
                startIndex = 0;
            }
            for (int i = 0, len = columnParamList.size(); i < len; i++) {
                ColumnParam columnParam = columnParamList.get(i);
                int index = startIndex + i + 1;
                if (StringUtils.isNotBlank(columnParam.getColumnDefinition()) && StringUtils.isNotBlank(columnParam.getColumnValue())) {
                    try {
                        if (columnParam.getDataType() < ResourceConstant.COLUMN_DATATYPE_DATE) {
                            // 字符类型
                            if (columnParam.getFuzzySearch()) {
                                //模糊查询
                                preparedStatement.setString(index, genLikeWord(columnParam.getColumnValue()));
                            } else {
                                //精确查询
                                preparedStatement.setString(index, columnParam.getColumnValue());
                            }
                        } else if (ResourceConstant.COLUMN_DATATYPE_DATE.equals(columnParam.getDataType())) {
                            // 时间类型date
                            String columnValue = columnParam.getColumnValue();
                            String[] str = columnValue.split(",");
                            if (str.length == 1) {
                                // 只选择了“开始时间”
                                preparedStatement.setString(index, str[0]);
                            } else if (str.length == 2) {
                                if (StringUtils.isBlank(str[0])) {
                                    // 只选择了“结束时间”
                                    preparedStatement.setString(index, str[1]);
                                } else {
                                    // 同时选择了“开始时间”和“结束时间”
                                    preparedStatement.setString(index, str[0]);
                                    preparedStatement.setString(index + 1, str[1]);
                                    startIndex++;
                                }
                            }
                        } else if (ResourceConstant.COLUMN_DATATYPE_INTEGER.equals(columnParam.getDataType())) {
                            // 整型（正则校验）
                            if (!Pattern.matches("^[0-9]*$", columnParam.getColumnValue())) {
                                throw new APIException(columnParam.getColumnDefinition() + " 需要为 纯数字");
                            }
                            preparedStatement.setLong(index, Long.parseLong(columnParam.getColumnValue()));
                        } else if (ResourceConstant.COLUMN_DATATYPE_DECIMAL.equals(columnParam.getDataType())) {
                            // 浮点型
                            if (!Pattern.matches("^(\\-|\\+)?\\d+(\\.\\d+)?$", columnParam.getColumnValue())) {
                                throw new APIException(columnParam.getColumnDefinition() + " 需要为 正数、负数或小数");
                            }
                            preparedStatement.setBigDecimal(index, new BigDecimal(columnParam.getColumnValue()));
                        }
                    } catch (SQLException e) {
                        System.out.println("preparedStatement设置参数值失败");
                        e.printStackTrace();
                    }
                }
            }
        }
        return preparedStatement;
    }

    /**
     * 生成模糊查询字段
     *
     * @param word 要匹配的字符串
     * @return 支持模糊查询格式的字符串
     */
    private static String genLikeWord(Object word) {
        return PERCENT_CHAR + word + PERCENT_CHAR;
    }

    /**
     * 执行查询
     *
     * @param databaseInfoVo
     * @param databaseType
     * @param sql
     * @return
     * @throws APIException
     * @author lwx
     */
    public static DataWithColumn executeQueryBeta(List<DlRescataColumn> dlRescataColumnList, String search, List<ColumnParam> columnParamList,
                                                  DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException {
        List<String> columnNameList = new ArrayList<>();
        List<Object[]> objectList = new ArrayList<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 设置数据
            fillData(dlRescataColumnList, search, columnParamList, preparedStatement);
            System.out.println(preparedStatement.toString());
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            // 统计 字段总数
            int columnCount = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Object[] object = new Object[columnCount];
                for (int i = 0; i < columnCount; i++) {
                    int columnType = resultSetMetaData.getColumnType(i + 1);
                    Object objecti = null;
                    switch (columnType) {
                        case Types.DATE:
                            if(resultSet.getDate(i + 1)!=null){
                                objecti = resultSet.getDate(i + 1).toString();
                            }
                            break;
                        case Types.TIMESTAMP:
                            objecti = DatabaseDataTypeTransferUtils.toJdbcTimestamp(objecti);
                            break;
                        case Types.CLOB:
                            objecti = DatabaseDataTypeTransferUtils.clobToString(objecti);
                            break;
                        default:
                            objecti = resultSet.getString(i + 1);
                            break;
                    }

//                    if (Types.BIGINT == columnType && ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
//                        try {
//                            objecti = resultSet.getBigDecimal(i + 1).longValue();
//                        } catch (SQLException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (objecti == null) {
//                        objecti = resultSet.getObject(i + 1);
//                    }
//                    if (objecti instanceof oracle.sql.TIMESTAMP) {
//                        objecti = DatabaseDataTypeTransferUtils.toJdbcTimestamp(objecti);
//                    } else if (objecti instanceof oracle.sql.CLOB || objecti instanceof java.sql.Clob) {
//                        objecti = DatabaseDataTypeTransferUtils.clobToString(objecti);
//                    }
                    object[i] = objecti;
                }
                objectList.add(object);
            }
            for (int i = 0; i < columnCount; i++) {
                columnNameList.add(resultSetMetaData.getColumnName(i + 1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_SERVER_ERR, "查询SQL 执行有误");
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
    public static Long executeCountBeta(List<ColumnParam> paramList, DatabaseInfoVo databaseInfoVo, Long databaseType, String sql) throws APIException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            // 获取数据库连接
            connection = JdbcUtils.getConnection(databaseInfoVo, databaseType);
            // 预编译
            preparedStatement = connection.prepareStatement(sql);
            // 设置参数
            fillData(Collections.emptyList(), "", paramList, preparedStatement);
            String s = preparedStatement.toString();
            System.out.println(s);
            // 执行查询
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new APIException("统计SQL 执行有误");
        } finally {
            JdbcUtils.release(resultSet, preparedStatement, connection);
        }
        return null;
    }

    /**
     * 不用数据库中存的字段名，用DatabaseMetaData 拿回来的，拿回来的会有大小写区分，后续select sql 在列上都加双引号
     * @param selectColumnList
     * @param resourceTableName
     * @param databaseInfoVo
     * @return
     * @throws APIException
     */
    public static void amemdRealSelectAndWhereColumnList(List<DlRescataColumn> selectColumnList, List<ColumnParam> columnParamList, StringBuffer resourceTableName, DatabaseInfoVo databaseInfoVo) throws APIException {
        Connection connection = JdbcUtils.getConnection(databaseInfoVo, databaseInfoVo.getDatabaseType());
        try {
//            if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseInfoVo.getDatabaseType())) {
//                OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);
//                oracleConnection.setIncludeSynonyms(true);
//                connection = oracleConnection;
//            }
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            // 拿出所有表名
            ResultSet tables = databaseMetaData.getTables(connection.getCatalog(), connection.getSchema(), null, new String[]{"TABLE","VIEW"});
            // 修正表名大小写
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                if (resourceTableName != null && tableName != null && resourceTableName.toString().toUpperCase().equals(tableName.toUpperCase())) {
                    resourceTableName.replace(0, resourceTableName.length(), tableName);
                    break;
                }
            }
            // 拿出所有字段名
            ResultSet metaDataColumns = databaseMetaData.getColumns(connection.getCatalog(), connection.getSchema(), resourceTableName.toString(), "%");
            // 修正字段名大小写
            while (metaDataColumns.next()) {
                //字段名
                String columnName = metaDataColumns.getString("COLUMN_NAME");
                //对应typesSql的值
                int javaTypesSql = metaDataColumns.getInt("DATA_TYPE");
                //字段长度
                int precision = metaDataColumns.getInt("COLUMN_SIZE");
                //精度
                int scale = metaDataColumns.getInt("DECIMAL_DIGITS");
                //是否为空：0就表示Not Null，1表示可以是Null
                int nullAble = metaDataColumns.getInt("NULLABLE");
                // 字段注释
                String remarks = metaDataColumns.getString("REMARKS");

                for (DlRescataColumn dlRescataColumn : selectColumnList) {
                    if (columnName != null && dlRescataColumn.getColumnDefinition() != null && columnName.toUpperCase().equals(dlRescataColumn.getColumnDefinition().toUpperCase())) {
                        dlRescataColumn.setColumnDefinition(columnName);
                        dlRescataColumn.setColumnComment(StringUtils.isNotBlank(remarks) ? remarks : dlRescataColumn.getColumnComment());
                        break;
                    }
                }
                for (ColumnParam columnParam : columnParamList) {
                    if (columnName != null && columnParam.getColumnDefinition() != null && columnName.toUpperCase().equals(columnParam.getColumnDefinition().toUpperCase())) {
                        columnParam.setColumnDefinition(columnName);
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args) {
        String tableName = "test_students";
        String sql = "SELECT COUNT (1) FROM pg_class WHERE relname = '" + tableName + "';";

        String driverClass = "org.postgresql.Driver";
        String url = "jdbc:postgresql://219.135.182.2:5432/dg12345?currentSchema=c##12345";
        String user = "mppuser";
        String password = "ioc123456";

        DatabaseInfoVo databaseInfoVo = new DatabaseInfoVo("219.135.182.2", "5432", "dg12345", "c##12345", user, password, null, null);

//        byte[] bytes = new byte[]{49,54,49,53,49,50};
        byte[] bytes = new byte[]{49};
        String s = new String(bytes);
        System.out.println("bytes:"+s);
        int idx = 0;
        long l = ((long) (bytes[idx + 0] & 255) << 56)
                + ((long) (bytes[idx + 1] & 255) << 48)
                + ((long) (bytes[idx + 2] & 255) << 40)
                + ((long) (bytes[idx + 3] & 255) << 32)
                + ((long) (bytes[idx + 4] & 255) << 24)
                + ((long) (bytes[idx + 5] & 255) << 16)
                + ((long) (bytes[idx + 6] & 255) << 8)
                + (bytes[idx + 7] & 255);
        System.out.println(l);

        Long count = null;
//        try {
//            count = executeCount(databaseInfoVo, 2L, sql);
//        } catch (APIException e) {
//            e.printStackTrace();
//        }
        System.out.println(count);
    }
}
