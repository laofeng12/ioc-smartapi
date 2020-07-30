package com.openjava.datalake.dataxjdbcutil.util;

import com.openjava.datalake.dataxjdbcutil.exception.DBUtilErrorCode;
import com.openjava.datalake.dataxjdbcutil.exception.DataXException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

import java.math.BigDecimal;
import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @Author xjd
 * @Date 2019/9/24 11:36
 * @Version 1.0
 */
@Log4j2
public class DbUtil {

    public static Map<String, HikariDataSource> hikariDataSourcePool = new ConcurrentHashMap<>();
    public static final int cpuCodeNum = Runtime.getRuntime().availableProcessors();

    private static HikariConfig initHikariConfig(String jdbcUrl, String username, String password, String driverClassName, String poolNameKey) {

        Properties props = new Properties();
        // 适配oracle 可以拿到Remark（ 表和列的注释）
        props.setProperty("remarks", "true");
        props.setProperty("useInformationSchema", "true");
        props.setProperty("remarksReporting","true");

        try {
            Class.forName(driverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(jdbcUrl);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setPoolName(poolNameKey);
        hikariConfig.setDataSourceProperties(props);
        log.info("cpuCodeNum:[{}]", cpuCodeNum);
        log.info("HikariMaxPoolSize:[{}]", (cpuCodeNum * 2 + 1));
        System.out.println("cpuCodeNum:"+cpuCodeNum);
        System.out.println("HikariMaxPoolSize:"+(cpuCodeNum * 2 + 1));
        /**
         公式：connections =（（core_count * 2）+ effective_spindle_count）
         effective_spindle_count is the number of disks in a RAID.就是磁盘列阵中的硬盘数
         */
//        hikariConfig.setMaximumPoolSize(cpuCodeNum * 2 + 1);
        // 计算服务器核心数不对，先写死
        hikariConfig.setMaximumPoolSize(20);
        // 暂时设置跟最大链接数不一样的值，为了让连接空闲时间生效
        hikariConfig.setMinimumIdle(10);
        // 建议不设置最小空闲连接数，默认就等于最大连接数
//        hikariConfig.setMinimumIdle(cpuCodeNum*2 + 1);
        // 链接超时设置为5秒，缺省：30s
        hikariConfig.setConnectionTimeout(TimeUnit.SECONDS.toMillis(5));
        hikariConfig.setValidationTimeout(TimeUnit.SECONDS.toMillis(1));
        // JDBC4以上不建议设置测试链接sql，设置 validationTime=1后会用connection.isValid(1);
//            hikariConfig.setConnectionTestQuery("SELECT 1");
        // 一个连接idle状态的最大时长（毫秒），超时则被释放（retired），缺省:10分钟
//            hikariConfig.setIdleTimeout(TimeUnit.SECONDS.toMillis(1));
        // 清理一分钟不活跃的链接
        hikariConfig.setIdleTimeout(TimeUnit.MINUTES.toMillis(1));
        // 10分钟最大生命周期，
        hikariConfig.setMaxLifetime(TimeUnit.MINUTES.toMillis(10));
        // 开启连接监测泄露
//        hikariConfig.setLeakDetectionThreshold(TimeUnit.SECONDS.toMillis(5));

        return hikariConfig;
    }

    private static HikariDataSource getHikariDataSource(String poolNameKey, HikariConfig hikariConfig) {
        HikariDataSource hikariDataSource = hikariDataSourcePool.get(poolNameKey);
        if (hikariDataSource == null) {
            synchronized (DbUtil.class) {
                if (hikariDataSource == null) {
                    try {
                        hikariDataSource = new HikariDataSource(hikariConfig);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new DataXException(DBUtilErrorCode.COMMON_DB_CONN_ERROR, e.getMessage());
                    }
                    hikariDataSourcePool.put(poolNameKey, hikariDataSource);
                }
            }
        }
        return hikariDataSource;
    }
    public static Connection getConnByHikar(String jdbcUrl, String username, String password, String driverClassName) {
        String poolNameKey = jdbcUrl + username;
        HikariConfig hikariConfig = initHikariConfig(jdbcUrl, username, password, driverClassName, poolNameKey);
        HikariDataSource hikariDataSource = getHikariDataSource(poolNameKey, hikariConfig);
        Connection connection = null;
        try {
            connection = hikariDataSource.getConnection();
//            if (connection instanceof OracleConnection || hikariDataSource.isWrapperFor(OracleDataSource.class) || connection.isWrapperFor(OracleConnection.class)) {//设置Oracle数据库的表注释可读
//                ((OracleConnection) connection).setRemarksReporting(true);//设置连接属性,使得可获取到表的REMARK(备注)
//            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw DataXException.asDataXException(DBUtilErrorCode.COMMON_DB_CONN_ERROR, DBUtilErrorCode.COMMON_DB_CONN_ERROR.getDescription());
        }
        return connection;
    }

    public static Connection getConnByHikar(HikariConfig hikariConfig) {
        String poolNameKey = "";
        String poolName = hikariConfig.getPoolName();
        if (StringUtils.isBlank(poolName)) {
            String jdbcUrl = hikariConfig.getJdbcUrl();
            String username = hikariConfig.getUsername();
            poolNameKey = jdbcUrl + username;
        } else {
            poolNameKey = poolName;
        }
        HikariDataSource hikariDataSource = getHikariDataSource(poolNameKey, hikariConfig);

        Connection connection = null;
        try {
            connection = hikariDataSource.getConnection();
        } catch (SQLException e) {
            throw DataXException.asDataXException(DBUtilErrorCode.COMMON_DB_CONN_ERROR, DBUtilErrorCode.COMMON_DB_CONN_ERROR.getDescription());
        }
        return connection;
    }

    /**
     * a wrapped method to execute select-like sql statement .
     *
     * @param conn         Database connection .
     * @param sql          sql statement to be executed
     * @param fetchSize
     * @param queryTimeout unit:second
     * @return
     * @throws SQLException
     */
    public static ResultSet query(Connection conn, String sql, int fetchSize, int queryTimeout)
            throws SQLException {
        // make sure autocommit is off
        conn.setAutoCommit(false);
        // TYPE_FORWARD_ONLY 设置游标只能往下移动，之前访问过的数据就释放内存了
        Statement stmt = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
        // 内存中缓存的记录数，设置值大可以减少跟数据库的交互，减少io，占用内存
        stmt.setFetchSize(fetchSize);
        stmt.setQueryTimeout(queryTimeout);
        return query(stmt, sql);
    }

    /**
     * a wrapped method to execute select-like sql statement .
     *
     * @param stmt {@link Statement}
     * @param sql  sql statement to be executed
     * @return a {@link ResultSet}
     * @throws SQLException if occurs SQLException.
     */
    public static ResultSet query(Statement stmt, String sql)
            throws SQLException {
        return stmt.executeQuery(sql);
    }

    public static void  deleteAllFromTableWithoutCloseConn(Connection conn, String tableName) throws SQLException {
        String deleteTemplate = "delete from %s ";
        String checkDeletePrivilegeSQL = String.format(deleteTemplate, tableName);
        executeSqls(conn, new ArrayList<>(Arrays.asList(checkDeletePrivilegeSQL)));
    }

    public static void executeSqls(Connection conn, List<String> sqls) throws SQLException {
        Statement stmt = null;
        try {
            stmt = conn.createStatement();
            for (String sql : sqls) {
                executeSqlWithoutResultSet(stmt, sql);
            }
        } catch (SQLException e) {
            throw e;
        } finally {
            JdbcUtils.closeStatement(stmt);
        }
    }

    public static void executeSqlWithoutResultSet(Statement stmt, String sql)
            throws SQLException {
        stmt.execute(sql);
    }

    public static Class<? extends Object> toJavaType(int javaSqlType) {
        switch (javaSqlType) {

            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                Class<String> stringClass = String.class;
                return stringClass;

            case Types.CLOB:
            case Types.NCLOB:
                return String.class;

            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.INTEGER:
            case Types.BIGINT:
                return Long.class;

            case Types.NUMERIC:
            case Types.DECIMAL:
                return Double.class;

            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                return Double.class;

            case Types.TIME:
                return Date.class;

            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                return Date.class;

            case Types.TIMESTAMP:
                return Date.class;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                return Byte.class;

            // warn: bit(1) -> Types.BIT 可使用BoolColumn
            // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
            case Types.BOOLEAN:
            case Types.BIT:
                return Boolean.class;

            case Types.NULL:
                return String.class;

            default:
                return null;
        }
    }
    public static String resultGetToString(int javaSqlType, ResultSet resultSet, int columnIndex) throws SQLException {
        String stringValue = null;
        switch (javaSqlType) {

            case Types.CHAR:
            case Types.NCHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                stringValue = resultSet.getString(columnIndex);
                break;
            case Types.CLOB:
            case Types.NCLOB:
                stringValue = resultSet.getString(columnIndex);
                break;
            case Types.SMALLINT:
            case Types.TINYINT:
            case Types.INTEGER:
            case Types.BIGINT:
                stringValue = String.valueOf(resultSet.getLong(columnIndex));
                break;
            case Types.NUMERIC:
            case Types.DECIMAL:
                stringValue = new BigDecimal(resultSet.getDouble(columnIndex)).toString();
                break;
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                stringValue = new BigDecimal(resultSet.getDouble(columnIndex)).toString();
                break;
            case Types.TIME:
                Time time = resultSet.getTime(columnIndex);
                stringValue = time.toLocalTime().format(DateTimeFormatter.BASIC_ISO_DATE);
                break;
            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                stringValue = resultSet.getDate(columnIndex).toLocalDate().format(DateTimeFormatter.BASIC_ISO_DATE);
                break;
            case Types.TIMESTAMP:
                Timestamp timestamp = resultSet.getTimestamp(columnIndex);
                stringValue = timestamp.toLocalDateTime().format(DateTimeFormatter.BASIC_ISO_DATE);
                break;
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                byte[] bytes = resultSet.getBytes(columnIndex);
                stringValue =  new String(bytes);
                break;
            // warn: bit(1) -> Types.BIT 可使用BoolColumn
            // warn: bit(>1) -> Types.VARBINARY 可使用BytesColumn
            case Types.BOOLEAN:
            case Types.BIT:
                boolean aBoolean = resultSet.getBoolean(columnIndex);
                stringValue = String.valueOf(aBoolean);
                break;

            case Types.NULL:
                stringValue = "";
                break;
            default:
                break;
        }
        return stringValue;
    }
}
