package com.openjava.datalake.common;

/**
 * @Author xjd
 * @Date 2020/3/10 17:47
 * @Version 1.0
 */
public class DtsConstants {
    /**
     * 数据库类型（ 0:Oracle,1:MySql高版本 2：Mysql低版本 3:PostgreSQL  4:hive  5:SQL Server 6:华为hive，7:GAUSSDB）
     */
    public static final Integer DATABASE_TYPE_ORACLE = 0;
    public static final Integer DATABASE_TYPE_MYSQL_NEW = 1;
    public static final Integer DATABASE_TYPE_MYSQL_OLD = 2;
    public static final Integer DATABASE_TYPE_POSTGRES = 3;
    public static final Integer DATABASE_TYPE_HIVE = 4;
    public static final Integer DATABASE_TYPE_SQLSERVER = 5;
    public static final Integer DATABASE_TYPE_HIVE_HUAWEI = 6;
    public static final Integer DATABASE_TYPE_GAUSSDB = 7;
    public static final Integer DATABASE_TYPE_TXT = 8;
}
