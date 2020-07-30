package com.openjava.datalake.util;

import lombok.Data;

/**
 * @Author JiaHai
 * @Description 华为HIVE连接工具类
 */
@Data
public class HiveUtils {

    private static final String ZOOKEEPER_DEFAULT_LOGIN_CONTEXT_NAME = "Client";
    private static final String ZOOKEEPER_SERVER_PRINCIPAL_KEY = "zookeeper.server.principal";
    private static final String ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL = "zookeeper/hadoop.hadoop.com";

//    private static Configuration CONF = null;
//    private static String KRB5_FILE = null;
//    private static String USER_NAME = null;
//    private static String USER_KEYTAB_FILE = null;
//
//    private static String zkQuorum = null;//zookeeper节点ip和端口列表
//    private static String auth = null;
//    private static String sasl_qop = null;
//    private static String zooKeeperNamespace = null;
//    private static String serviceDiscoveryMode = null;
//    private static String principal = null;
//    private static String database = null;
//
//    private static String env;
//
//    private static String url;//数据库连接地址
//    private static String username = "";//用户名
//    private static String password = "";//密码
//    private static String driver;//数据库连接驱动
//
//    private Connection conn = null;
//    private PreparedStatement preparedStatement = null;
//    private ResultSet resultSet = null;
//    private Statement statement = null;
//
//    static {
//        try {
//            CONF = new Configuration();
//            Properties clientInfo;
//            InputStream fileInputStream = null;
//
//            try {
//                clientInfo = new Properties();
//                //"db.properties"为客户端配置文件，如果使用多实例特性，需要把该文件换成对应实例客户端下的"hiveclient.properties"
//                //"db.properties"文件位置在对应实例客户端安裝包解压目录下的config目录下
//                String hiveclientProp = ResourceUtils.getFile("classpath:db.properties").getAbsolutePath();
//                File propertiesFile = new File(hiveclientProp);
//                fileInputStream = new FileInputStream(propertiesFile);
//                clientInfo.load(fileInputStream);
//            } catch (Exception e) {
//                throw new IOException(e);
//            } finally {
//                if (fileInputStream != null) {
//                    fileInputStream.close();
//                    fileInputStream = null;
//                }
//            }
//            env = clientInfo.getProperty("env");
//            if ("dev".equals(env)) {
//                url = clientInfo.getProperty("hive.url");
//                username = clientInfo.getProperty("hive.username");
//                password = clientInfo.getProperty("hive.password");
//            } else {
//                //zkQuorum获取后的格式为"xxx.xxx.xxx.xxx:24002,xxx.xxx.xxx.xxx:24002,xxx.xxx.xxx.xxx:24002";
//                //"xxx.xxx.xxx.xxx"为集群中ZooKeeper所在节点的业务IP，端口默认是24002
//                zkQuorum = clientInfo.getProperty("zk.quorum");
//                auth = clientInfo.getProperty("auth");
//                sasl_qop = clientInfo.getProperty("sasl.qop");
//                zooKeeperNamespace = clientInfo.getProperty("zooKeeperNamespace");
//                serviceDiscoveryMode = clientInfo.getProperty("serviceDiscoveryMode");
//                principal = clientInfo.getProperty("principal");
//                database = clientInfo.getProperty("database");
//                // 设置新建用户的USER_NAME，其中"xxx"指代之前创建的用户名，例如创建的用户为user，则USER_NAME为user
//                USER_NAME = clientInfo.getProperty("username");
//
//                if ("KERBEROS".equalsIgnoreCase(auth)) {
//                    // 设置客户端的keytab和krb5文件路径
//                    USER_KEYTAB_FILE = ResourceUtils.getFile("classpath:user.keytab").getAbsolutePath().replace("\\", "/");
//                    System.out.println(USER_KEYTAB_FILE);
//                    KRB5_FILE = ResourceUtils.getFile("classpath:krb5.conf").getAbsolutePath().replace("\\", "/");
//                    System.setProperty("java.security.krb5.conf", KRB5_FILE);
//                    System.setProperty(ZOOKEEPER_SERVER_PRINCIPAL_KEY, ZOOKEEPER_DEFAULT_SERVER_PRINCIPAL);
//                }
//                // 拼接JDBC URL
//                StringBuilder sBuilder = new StringBuilder("jdbc:hive2://").append(zkQuorum).append("/");
//
//                if ("KERBEROS".equalsIgnoreCase(auth)) {
//                    sBuilder.append(";serviceDiscoveryMode=")
//                            .append(serviceDiscoveryMode)
//                            .append(";zooKeeperNamespace=")
//                            .append(zooKeeperNamespace)
//                            .append(";sasl.qop=")
//                            .append(sasl_qop)
//                            .append(";auth=")
//                            .append(auth)
//                            .append(";principal=")
//                            .append(principal)
//                            .append(";user.principal=")
//                            .append(USER_NAME)
//                            .append(";user.keytab=")
//                            .append(USER_KEYTAB_FILE)
//                            .append(";");
//                } else {
//                    //普通模式
//                    sBuilder.append(";serviceDiscoveryMode=")
//                            .append(serviceDiscoveryMode)
//                            .append(";zooKeeperNamespace=")
//                            .append(zooKeeperNamespace)
//                            .append(";auth=none");
//                }
//                url = sBuilder.toString();
//            }
//
//            // 加载Hive JDBC驱动
//            driver = clientInfo.getProperty("hive.driver");
//            Class.forName(driver);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public HiveUtils() {
//        try {
//            // 如果使用的是普通模式，那么第二个参数需要填写正确的用户名，否则会以匿名用户(anonymous)登录
//            conn = DriverManager.getConnection(url, username, password);
//            statement = conn.createStatement();
//            if (!"dev".equals(env)) {
//                //如果连接的华为hive，切换到默认要使用的数据库
//                changeDatabase(database);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 6、切换数据库
//     *
//     * @param database 数据库名称
//     *                 语句：use {database}
//     */
//    public void changeDatabase(String database) throws Exception {
//        try {
//            statement.execute("use " + database);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 创建数据库 - 用户注册时调用
//     *
//     * @param databaseName 根据用户标识生成的数据库名称
//     */
//    public void createDatabase(String databaseName) throws Exception {
//        try {
//            statement.execute("create database " + databaseName);
//        } catch (SQLException e) {
//            // TODO 自动生成的 catch 块
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 2. 数据库预览
//     *
//     * @return 返回数据库名称集合
//     * 语句：show databases
//     */
//    public List<String> getDatabases() throws Exception {
//        List result = new ArrayList<String>();
//
//        //执行查询
//        try {
//            preparedStatement = conn.prepareStatement("show databases");
//            resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                result.add(resultSet.getString(1));
//            }
//            return result;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
////            return null;
//        }
//    }
//
//    /**
//     * 3、数据库表预览
//     * show tables
//     *
//     * @param database 要获取哪个数据库中的表
//     * @return 返回的是数据表的集合
//     * 语句：show {database}.{table}
//     */
//    public List<String> getTables(String database) throws Exception {
//        List result = new ArrayList<String>();
//
//        //执行查询
//        try {
//            //切换数据库
//            changeDatabase(database);
//            resultSet = statement.executeQuery("show tables");
//            while (resultSet.next()) {
//                result.add(resultSet.getString(1));
//            }
//            return result;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
////            return null;
//        }
//    }
//
//    /**
//     * 4、 数据表结构预览
//     *
//     * @param database 数据库名称
//     * @param table    数据表名称
//     * @return 返回的是表的描述性息的集合
//     * 语句：desc {database}.{table}
//     */
//    //需要参数：数据库名，表名
//    public List<String> getTableDesc(String database, String table) throws Exception {
//        List<String> result = new ArrayList<>();
//
//        try {
//            //创建preparedStatement
//            preparedStatement = conn.prepareStatement("desc " + database + "." + table);
//            //执行查询操作
//            ResultSet resultSet = preparedStatement.executeQuery();
//
//            //将结果封装到list中
//            while (resultSet.next()) {
//                result.add(resultSet.getString(1) + "  " + resultSet.getString(2));
//            }
//
//            return result;
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
////        return null;
//    }
//
//    /**
//     * 5. 样例数据预览(抽样)——>limit实现
//     *
//     * @param database 数据库名称
//     * @param table    数据表名称
//     * @param n        获取前n行记录
//     *                 语句：select * from {database}.{table} limit {number}
//     */
//    public List<String> getDataByLimit(String database, String table, int n) throws Exception {
//        int columns = getTableDesc(database, table).size();
//        List<String> list = new ArrayList<>();
//        try {
//            preparedStatement = conn.prepareStatement("select * from " + database + "." + table + " limit " + n);
//            resultSet = preparedStatement.executeQuery();
//            return getData(resultSet, columns);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
////        return null;
//    }
//
//    /**
//     * 5. 样例数据预览(抽样)——> tablesample实现
//     *
//     * @param database 数据库名称
//     * @param table    数据表
//     * @param percent  抽样比例
//     *                 语句: select * from {database}.{table} tablesample({percent} percent)
//     */
//    public List<String> getDataBySample(String database, String table, double percent) throws Exception {
//        int columns = getTableDesc(database, table).size();
//        List<String> list = new ArrayList<>();
//        try {
//            preparedStatement = conn.prepareStatement("select * from " + database + "." + table + " tablesample(" + percent + " percent)");
//            resultSet = preparedStatement.executeQuery();
//            return getData(resultSet, columns);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
////        return null;
//    }
//
//    /**
//     * 5. 样例数据预览(抽样)——> rand实现
//     *
//     * @param database 数据库名称
//     * @param table    数据表
//     * @param n        随机取出前n行记录
//     *                 语句：select *,rand()r from {database}.{table} order by r limit n
//     */
//    public List<String> getDataByRandom(String database, String table, int n) throws Exception {
//        int columns = getTableDesc(database, table).size();
//        List<String> list = new ArrayList<>();
//        try {
//            preparedStatement = conn.prepareStatement("select *,rand() r from " + database + "." + table + " order by r limit " + n);
//            resultSet = preparedStatement.executeQuery();
//            return getData(resultSet, columns - 1);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
////        return null;
//    }
//
//    /**
//     * 数据封装工具
//     *
//     * @param resultSet 结果集
//     * @param size      数据列数量,也就是要获取几列的数据
//     * @return 封装的List数据集
//     */
//    private List<String> getData(ResultSet resultSet, int size) throws Exception {
//        try {
//            List<String> list = new ArrayList<>();
//            while (resultSet.next()) {
//                String line = "";
//                for (int i = 0; i < size; i++) {
//                    line += resultSet.getString(i + 1) + "\t";
//                }
//                line = line.substring(0, line.length() - 1);        //左闭右开
//                //将每一条记录添加到lisi中
//                list.add(line);
//            }
//            return list;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
////            return null;
//        }
//    }
//
//    /**
//     * 通过自定义的SQL返回结果
//     *
//     * @param sql 要执行的sql语句——包含数据库名称
//     * @return 执行SQL的结果集
//     * <p>
//     * 前期准备：hive中存在数据库temp，用于存放临时SQL去查询的中间表
//     * ① 将查询到的结果集生成中间表存放到temp数据库下 create table {table} as sql
//     * ② 再通过查询此中间表返回结果集合
//     * ③ 表名随机：tmp_userId_时间戳
//     */
//    public List<String> getDataBySQL(String sql) throws Exception {
//        //将表名进行随机：tmp_userId_时间戳
//        //时间戳——> long ——> 1970-01-01 00:00:00 ——> Unix元年
//
//        try {
//            String tableName = "temp_1_" + System.currentTimeMillis();
//            preparedStatement = conn.prepareStatement("create table temp." + tableName + " as " + sql);
//            preparedStatement.execute();
//
//            //获取表的字段个数
//            int size = getTableDesc("temp", tableName).size();
//            preparedStatement = conn.prepareStatement("select * from temp." + tableName);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            return getData(resultSet, size);
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//
////        return null;
//    }
//
//
//    /**
//     * 从数据表中查询指定列的数据
//     *
//     * @param sql          要执行的sql语句——>包含数据库名称
//     * @param columnsIndex 待查询的列的索引
//     * @return SQL执行的结果集
//     */
//    public List<String> getDataBySQLWithColumns(String sql, int[] columnsIndex) throws Exception {
//        try {
//            String tableName = "tmp_1_" + System.currentTimeMillis();
//            preparedStatement = conn.prepareStatement("create table temp." + tableName + " as " + sql);
//            resultSet = preparedStatement.executeQuery();
//            return getDataByColumns(resultSet, columnsIndex);
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
////            return null;
//        }
//    }
//
//    /**
//     * @param resultSet   查询结果集
//     * @param columnIndex 数据列索引
//     * @return 封装的List数据集
//     */
//    private List<String> getDataByColumns(ResultSet resultSet, int[] columnIndex) throws Exception {
//        List<String> list = new ArrayList<>();
//
//        try {
//            while (resultSet.next()) {
//                String line = "";
//
//                for (int i = 0; i < columnIndex.length; i++) {
//                    line += resultSet.getString(columnIndex[i]) + ",";
//                }
//                line = line.substring(0, line.length() - 1);
//                list.add(line);
//            }
//            return list;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
////            return null;
//        }
//    }
//
//    /**
//     * 执行查询语句，返回结果
//     *
//     * @param sql   需要执行的sql语句
//     * @param paras 执行查询时所需要的参数
//     * @return 结果集
//     */
//    public ResultSet executeQuery(String sql, Object[] paras) throws Exception {
//        try {
//            preparedStatement = conn.prepareStatement(sql);
//            getPreparedStatement(paras);
//            return preparedStatement.executeQuery();
//        } catch (SQLException e) {
//            e.printStackTrace();
////            return null;
//            throw e;
//        }
//    }
//
//    /**
//     * 数据的查询方法
//     *
//     * @param sql//查询的SQL语句
//     * @param paras//参数列表
//     * @return 返回List<Map                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               <                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               String                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               ,                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               Object>>结果集
//     */
//    public List<Map<String, Object>> queryForList(String sql, Object[] paras) throws Exception {
//        List<Map<String, Object>> mapList = new ArrayList<>();
//        try {
//            resultSet = executeQuery(sql, paras);
//            //ResultSet有getMetaData()会返回数据的列
//            ResultSetMetaData rsmd = resultSet.getMetaData();
//            while (resultSet.next()) {
//                Map<String, Object> map = new HashMap<>();
//                for (int i = 0; i < rsmd.getColumnCount(); i++) {
//                    String col_name = rsmd.getColumnName(i + 1);
//                    Object col_value = resultSet.getObject(col_name);
//                    if (col_value == null) {
//                        col_value = "";
//                    }
//                    map.put(col_name, col_value);
//                }
//                mapList.add(map);
//            }
//            return mapList;
//        } catch (SQLException e) {
//            e.printStackTrace();
////            return null;
//            throw e;
//        }
//    }
//
//    /**
//     * 数据的查询方法
//     *
//     * @param sql//查询的SQL语句
//     * @param paras//参数列表
//     * @return 返回Map
//     */
//    public Map<String, Object> queryForMap(String sql, Object[] paras) throws Exception {
//        List<Map<String, Object>> list = queryForList(sql, paras);
////        System.out.println(JsonUtil.objectToJson(list));
//        if (!list.isEmpty()) {
//            return list.get(0);
//        }
//
//
//        return null;
//    }
//
//    /**
//     * 将传入的参数进行赋值，构建一个完整的PreparedStatement
//     *
//     * @param paras 传入的参数
//     */
//    private void getPreparedStatement(Object[] paras) throws Exception {
//        try {
//            if (paras != null) {
//                for (int i = 0; i < paras.length; i++) {
//                    preparedStatement.setObject(i + 1, paras[i]);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 执行一个不需要返回结果的命令
//     *
//     * @param sql   需要执行的sql
//     * @param paras 执行sql需要的参数
//     */
//    public void execute(String sql, Object[] paras) throws Exception {
//        try {
//            preparedStatement = conn.prepareStatement(sql);
//            getPreparedStatement(paras);
//            preparedStatement.execute();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 删除数据库表
//     *
//     * @param database 数据库名称
//     * @param table    表名称
//     */
//    public void dropTable(String database, String table) throws Exception {
//        try {
//            String sql = "drop table if exists " + database + "." + table;
//            statement.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 删除数据库表
//     *
//     * @param table 表名称
//     */
//    public void dropTable(String table) throws Exception {
//        try {
//            String sql = "drop table if exists " + table;
//            statement.execute(sql);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }
//
//    /**
//     * 释放资源
//     */
//    public void close() {
//        try {
//            if (resultSet != null) {
//                resultSet.close();
//            }
//            if (preparedStatement != null) {
//                preparedStatement.close();
//            }
//            if (statement != null) {
//                statement.close();
//            }
//            if (conn != null) {
//                conn.close();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 本示例演示了如何使用Hive JDBC接口来执行HQL命令<br>
//     */
//    public static void main(String[] args) throws Exception {
//
//        // 定义HQL，HQL为单条语句，不能包含“;”
////        String[] sqls = {"CREATE TABLE IF NOT EXISTS employees_info(id INT,name STRING)",
////                "SELECT COUNT(*) FROM employees_info", "DROP TABLE employees_info"};
//
//        String[] sqls = {"CREATE TABLE test_teachers(  teacher_id BIGINT,  teacher_name STRING,  teacher_sex STRING,  teacher_birth DATE,  teacher_address STRING,  teacher_height DOUBLE )",
//                "SELECT COUNT(*) FROM employees_info", "DROP TABLE test_teachers"};
//
//        HiveUtils util = new HiveUtils();
//        util.execute(sqls[0], null);
//        System.out.println("Create table success!");
//
////    util.queryForMap(sqls[1],null);
////    System.out.println("Query table success!");
//
//        // 删表
//        util.execute(sqls[2], null);
//        System.out.println("Delete table success!");
//
//        util.close();
//    }

}
