package com.openjava.datalake.dataxjdbcutil.util;

import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author xjd
 * @Date 2019/9/24 10:58
 * @Version 1.0
 */
public class DataLakeReader {

    public static Set<String> getAllPrimaryKey(Connection conn, String tableName, String primaryKeyColumnName) {
        Set<String> allPrimaryKeys = new HashSet<>();
        String sql = String.format("select %s from %s ", primaryKeyColumnName, tableName);
        ResultSet resultSet = null;
        try {
            resultSet = DbUtil.query(conn, sql, 100000, 5);
//            int columnType = resultSet.getMetaData().getColumnType(1);
//            Class<?> aClass = DbUtil.toJavaType(columnType);
            int javaTypesSql = resultSet.getMetaData().getColumnType(1);
            while (resultSet.next()) {
                String resultString = DbUtil.resultGetToString(javaTypesSql, resultSet, 1);
//                String string = resultSet.getString(1);
                allPrimaryKeys.add(resultString);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeResultSet(resultSet);
        }
        return allPrimaryKeys;
    }









}
