package com.openjava.datalake.util;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.openjava.datalake.common.PublicConstant;
import com.openjava.datalake.common.ResourceConstant;
import com.openjava.datalake.rescata.domain.DlRescataColumn;
import com.openjava.datalake.rescata.vo.DatabaseInfoVo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
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

    public static final String sqlKeyword="ABORT,ABS,ABSENT,ABSOLUTE,ACCESS,ACCORDING,ACTION,ADA,ADD,ADMIN,AFTER,AGGREGATE,ALL,ALLOCATE,ALSO,ALTER,ALWAYS,ANALYSE,ANALYZE,AND,ANY,ARE,ARRAY,ARRAY_AGG,ARRAY_MAX_CARDINALITY,AS,ASC,ASENSITIVE,ASSERTION,ASSIGNMENT,ASYMMETRIC,AT,ATOMIC,ATTRIBUTE,ATTRIBUTES,AUTHORIZATION,AVG,BACKWARD,BASE64,BEFORE,BEGIN,BEGIN_FRAME,BEGIN_PARTITION,BERNOULLI,BETWEEN,BIGINT,BINARY,BIT,BIT_LENGTH,BLOB,BLOCKED,BOM,BOOLEAN,BOTH,BREADTH,BY,CACHE,CALL,CALLED,CARDINALITY,CASCADE,CASCADED,CASE,CAST,CATALOG,CATALOG_NAME,CEIL,CEILING,CHAIN,CHAR,CHARACTER,CHARACTERISTICS,CHARACTERS,CHARACTER_LENGTH,CHARACTER_SET_CATALOG,CHARACTER_SET_NAME,CHARACTER_SET_SCHEMA,CHAR_LENGTH,CHECK,CHECKPOINT,CLASS,CLASS_ORIGIN,CLOB,CLOSE,CLUSTER,COALESCE,COBOL,COLLATE,COLLATION,COLLATION_CATALOG,COLLATION_NAME,COLLATION_SCHEMA,COLLECT,COLUMN,COLUMNS,COLUMN_NAME,COMMAND_FUNCTION,COMMAND_FUNCTION_CODE,COMMENT,COMMENTS,COMMIT,COMMITTED,CONCURRENTLY,CONDITION,CONDITION_NUMBER,CONFIGURATION,CONNECT,CONNECTION,CONNECTION_NAME,CONSTRAINT,CONSTRAINTS,CONSTRAINT_CATALOG,CONSTRAINT_NAME,CONSTRAINT_SCHEMA,CONSTRUCTOR,CONTAINS,CONTENT,CONTINUE,CONTROL,CONVERSION,CONVERT,COPY,CORR,CORRESPONDING,COST,COUNT,COVAR_POP,COVAR_SAMP,CREATE,CROSS,CSV,CUBE,CUME_DIST,CURRENT,CURRENT_CATALOG,CURRENT_DATE,CURRENT_DEFAULT_TRANSFORM_GROUP,CURRENT_PATH,CURRENT_ROLE,CURRENT_ROW,CURRENT_SCHEMA,CURRENT_TIME,CURRENT_TIMESTAMP,CURRENT_TRANSFORM_GROUP_FOR_TYPE,CURRENT_USER,CURSOR,CURSOR_NAME,CYCLE,DATA,DATABASE,DATALINK,DATE,DATETIME_INTERVAL_CODE,DATETIME_INTERVAL_PRECISION,DAY,DB,DEALLOCATE,DEC,DECIMAL,DECLARE,DEFAULT,DEFAULTS,DEFERRABLE,DEFERRED,DEFINED,DEFINER,DEGREE,DELETE,DELIMITER,DELIMITERS,DENSE_RANK,DEPTH,DEREF,DERIVED,DESC,DESCRIBE,DESCRIPTOR,DETERMINISTIC,DIAGNOSTICS,DICTIONARY,DISABLE,DISCARD,DISCONNECT,DISPATCH,DISTINCT,DLNEWCOPY,DLPREVIOUSCOPY,DLURLCOMPLETE,DLURLCOMPLETEONLY,DLURLCOMPLETEWRITE,DLURLPATH,DLURLPATHONLY,DLURLPATHWRITE,DLURLSCHEME,DLURLSERVER,DLVALUE,DO,DOCUMENT,DOMAIN,DOUBLE,DROP,DYNAMIC,DYNAMIC_FUNCTION,DYNAMIC_FUNCTION_CODE,EACH,ELEMENT,ELSE,EMPTY,ENABLE,ENCODING,ENCRYPTED,END,END-EXEC,END_FRAME,END_PARTITION,ENFORCED,ENUM,EQUALS,ESCAPE,EVENT,EVERY,EXCEPT,EXCEPTION,EXCLUDE,EXCLUDING,EXCLUSIVE,EXEC,EXECUTE,EXISTS,EXP,EXPLAIN,EXPRESSION,EXTENSION,EXTERNAL,EXTRACT,FALSE,FAMILY,FETCH,FILE,FILTER,FINAL,FIRST,FIRST_VALUE,FLAG,FLOAT,FLOOR,FOLLOWING,FOR,FORCE,FOREIGN,FORTRAN,FORWARD,FOUND,FRAME_ROW,FREE,FREEZE,FROM,FS,FULL,FUNCTION,FUNCTIONS,FUSION,GENERAL,GENERATED,GET,GLOBAL,GO,GOTO,GRANT,GRANTED,GREATEST,GROUP,GROUPING,GROUPS,HANDLER,HAVING,HEADER,HEX,HIERARCHY,HOLD,HOUR,ID,IDENTITY,IF,IGNORE,ILIKE,IMMEDIATE,IMMEDIATELY,IMMUTABLE,IMPLEMENTATION,IMPLICIT,IMPORT,IN,INCLUDING,INCREMENT,INDENT,INDEX,INDEXES,INDICATOR,INHERIT,INHERITS,INITIALLY,INLINE,INNER,INOUT,INPUT,INSENSITIVE,INSERT,INSTANCE,INSTANTIABLE,INSTEAD,INT,INTEGER,INTEGRITY,INTERSECT,INTERSECTION,INTERVAL,INTO,INVOKER,IS,ISNULL,ISOLATION,JOIN,KEY,KEY_MEMBER,KEY_TYPE,LABEL,LAG,LANGUAGE,LARGE,LAST,LAST_VALUE,LATERAL,LC_COLLATE,LC_CTYPE,LEAD,LEADING,LEAKPROOF,LEAST,LEFT,LENGTH,LEVEL,LIBRARY,LIKE,LIKE_REGEX,LIMIT,LINK,LISTEN,LN,LOAD,LOCAL,LOCALTIME,LOCALTIMESTAMP,LOCATION,LOCATOR,LOCK,LOWER,MAP,MAPPING,MATCH,MATCHED,MATERIALIZED,MAX,MAXVALUE,MAX_CARDINALITY,MEMBER,MERGE,MESSAGE_LENGTH,MESSAGE_OCTET_LENGTH,MESSAGE_TEXT,METHOD,MIN,MINUTE,MINVALUE,MOD,MODE,MODIFIES,MODULE,MONTH,MORE,MOVE,MULTISET,MUMPS,NAME,NAMES,NAMESPACE,NATIONAL,NATURAL,NCHAR,NCLOB,NESTING,NEW,NEXT,NFC,NFD,NFKC,NFKD,NIL,NO,NONE,NORMALIZE,NORMALIZED,NOT,NOTHING,NOTIFY,NOTNULL,NOWAIT,NTH_VALUE,NTILE,NULL,NULLABLE,NULLIF,NULLS,NUMBER,NUMERIC,OBJECT,OCCURRENCES_REGEX,OCTETS,OCTET_LENGTH,OF,OFF,OFFSET,OIDS,OLD,ON,ONLY,OPEN,OPERATOR,OPTION,OPTIONS,OR,ORDER,ORDERING,ORDINALITY,OTHERS,OUT,OUTER,OUTPUT,OVER,OVERLAPS,OVERLAY,OVERRIDING,OWNED,OWNER,PAD,PARAMETER,PARAMETER_MODE,PARAMETER_NAME,PARAMETER_ORDINAL_POSITION,PARAMETER_SPECIFIC_CATALOG,PARAMETER_SPECIFIC_NAME,PARAMETER_SPECIFIC_SCHEMA,PARSER,PARTIAL,PARTITION,PASCAL,PASSING,PASSTHROUGH,PASSWORD,PATH,PERCENT,PERCENTILE_CONT,PERCENTILE_DISC,PERCENT_RANK,PERIOD,PERMISSION,PLACING,PLANS,PLI,PORTION,POSITION,POSITION_REGEX,POWER,PRECEDES,PRECEDING,PRECISION,PREPARE,PREPARED,PRESERVE,PRIMARY,PRIOR,PRIVILEGES,PROCEDURAL,PROCEDURE,PROGRAM,PUBLIC,QUOTE,RANGE,RANK,READ,READS,REAL,REASSIGN,RECHECK,RECOVERY,RECURSIVE,REF,REFERENCES,REFERENCING,REFRESH,REGR_AVGX,REGR_AVGY,REGR_COUNT,REGR_INTERCEPT,REGR_R2,REGR_SLOPE,REGR_SXX,REGR_SXY,REGR_SYY,REINDEX,RELATIVE,RELEASE,RENAME,REPEATABLE,REPLACE,REPLICA,REQUIRING,RESET,RESPECT,RESTART,RESTORE,RESTRICT,RESULT,RETURN,RETURNED_CARDINALITY,RETURNED_LENGTH,RETURNED_OCTET_LENGTH,RETURNED_SQLSTATE,RETURNING,RETURNS,REVOKE,RIGHT,ROLE,ROLLBACK,ROLLUP,ROUTINE,ROUTINE_CATALOG,ROUTINE_NAME,ROUTINE_SCHEMA,ROW,ROWS,ROW_COUNT,ROW_NUMBER,RULE,SAVEPOINT,SCALE,SCHEMA,SCHEMA_NAME,SCOPE,SCOPE_CATALOG,SCOPE_NAME,SCOPE_SCHEMA,SCROLL,SEARCH,SECOND,SECTION,SECURITY,SELECT,SELECTIVE,SELF,SENSITIVE,SEQUENCE,SEQUENCES,SERIALIZABLE,SERVER,SERVER_NAME,SESSION,SESSION_USER,SET,SETOF,SETS,SHARE,SHOW,SIMILAR,SIMPLE,SIZE,SMALLINT,SNAPSHOT,SOME,SOURCE,SPACE,SPECIFIC,SPECIFICTYPE,SPECIFIC_NAME,SQL,SQLCODE,SQLERROR,SQLEXCEPTION,SQLSTATE,SQLWARNING,SQRT,STABLE,STANDALONE,START,STATE,STATEMENT,STATIC,STATISTICS,STDDEV_POP,STDDEV_SAMP,STDIN,STDOUT,STORAGE,STRICT,STRIP,STRUCTURE,STYLE,SUBCLASS_ORIGIN,SUBMULTISET,SUBSTRING,SUBSTRING_REGEX,SUCCEEDS,SUM,SYMMETRIC,SYSID,SYSTEM,SYSTEM_TIME,SYSTEM_USER,TABLE,TABLES,TABLESAMPLE,TABLESPACE,TABLE_NAME,TEMP,TEMPLATE,TEMPORARY,TEXT,THEN,TIES,TIME,TIMESTAMP,TIMEZONE_HOUR,TIMEZONE_MINUTE,TO,TOKEN,TOP_LEVEL_COUNT,TRAILING,TRANSACTION,TRANSACTIONS_COMMITTED,TRANSACTIONS_ROLLED_BACK,TRANSACTION_ACTIVE,TRANSFORM,TRANSFORMS,TRANSLATE,TRANSLATE_REGEX,TRANSLATION,TREAT,TRIGGER,TRIGGER_CATALOG,TRIGGER_NAME,TRIGGER_SCHEMA,TRIM,TRIM_ARRAY,TRUE,TRUNCATE,TRUSTED,TYPE,TYPES,UESCAPE,UNBOUNDED,UNCOMMITTED,UNDER,UNENCRYPTED,UNION,UNIQUE,UNKNOWN,UNLINK,UNLISTEN,UNLOGGED,UNNAMED,UNNEST,UNTIL,UNTYPED,UPDATE,UPPER,URI,USAGE,USER,USER_DEFINED_TYPE_CATALOG,USER_DEFINED_TYPE_CODE,USER_DEFINED_TYPE_NAME,USER_DEFINED_TYPE_SCHEMA,USING,VACUUM,VALID,VALIDATE,VALIDATOR,VALUE,VALUES,VALUE_OF,VARBINARY,VARCHAR,VARIADIC,VARYING,VAR_POP,VAR_SAMP,VERBOSE,VERSION,VERSIONING,VIEW,VOLATILE,WHEN,WHENEVER,WHERE,WHITESPACE,WIDTH_BUCKET,WINDOW,WITH,WITHIN,WITHOUT,WORK,WRAPPER,WRITE,XML,XMLAGG,XMLATTRIBUTES,XMLBINARY,XMLCAST,XMLCOMMENT,XMLCONCAT,XMLDECLARATION,XMLDOCUMENT,XMLELEMENT,XMLEXISTS,XMLFOREST,XMLITERATE,XMLNAMESPACES,XMLPARSE,XMLPI,XMLQUERY,XMLROOT,XMLSCHEMA,XMLSERIALIZE,XMLTABLE,XMLTEXT,XMLVALIDATE,YES,ZONE";

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
            throw new APIException(APIConstants.CODE_PARAM_ERR,"databaseType 不可为空");
        }
        if (StringUtils.isBlank(tableName)) {
            throw new APIException(APIConstants.CODE_PARAM_ERR,"tableName 不可为空");
        }
        String sql = "CREATE TABLE ";
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            sql += tableName;
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            if (null == databaseInfoVo) {
                throw new APIException(APIConstants.CODE_PARAM_ERR,"databaseInfo 不可为空");
            }
            sql += "\"" + databaseInfoVo.getSchema() + "\"." + tableName;
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            if (null == databaseInfoVo) {
                throw new APIException(APIConstants.CODE_PARAM_ERR,"databaseInfo 不可为空");
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
            throw new APIException(APIConstants.CODE_PARAM_ERR,"字段不可为空");
        }
        List<String> columnSqlList = new ArrayList<>();
        dlRescataColumnList.stream().forEach(dlRescataColumn -> {
            try {
                DdlSqlUtils.isDBFieldThrowAnException(dlRescataColumn.getColumnDefinition(),30);
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
            throw new APIException(APIConstants.CODE_PARAM_ERR,"dataType 不可为空");
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
     * 生成 修改字段注释的SQL
     *
     * @param databaseType        数据库类型
     * @param tableName           表名
     * @param dlRescataColumnList 字段信息列表
     * @return
     */
    public static List<String> updateCommentColumnSql(Long databaseType, String tableName, List<DlRescataColumn> dlRescataColumnList) {
        List<String> commentColumnSqlList = new ArrayList<>();
        if (databaseType.equals(ResourceConstant.DATABASE_TYPE_HIVE)) {

        } else if (databaseType.equals(ResourceConstant.DATABASE_TYPE_MPP) || databaseType.equals(ResourceConstant.DATABASE_TYPE_ORACLE)) {
            dlRescataColumnList.stream().forEach(dlRescataColumn -> {
                if (StringUtils.isNotBlank(dlRescataColumn.getColumnName())) {
                    commentColumnSqlList.add("COMMENT ON COLUMN " + tableName + "." + dlRescataColumn.getColumnDefinition().toLowerCase() + " IS '" + dlRescataColumn.getColumnName() + "'");
                }
            });
        }
        System.out.println("update comment on column：" + JSON.toJSON(commentColumnSqlList));
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
//            if (isUsingGIN) {
//                //gin索引
//                indexSql.append(tableName).append("_").append(columnName).append(" ON ")
//                        .append(tableSchema).append(" USING GIN(").append(columnName).append(")");
//            } else {
                //b-tree索引
                indexSql.append(tableName).append("_").append(columnName).append(" ON ")
                        .append(tableSchema).append(" USING BTREE (").append(columnName).append(")");
//            }
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
            throw new APIException(APIConstants.CODE_PARAM_ERR,"databaseType 不可为空");
        }
        if (StringUtils.isBlank(lastTableName) || StringUtils.isBlank(newTableName)) {
            throw new APIException(APIConstants.CODE_PARAM_ERR,"表名不可为空");
        }
        if (null == databaseInfoVo) {
            throw new APIException(APIConstants.CODE_PARAM_ERR,"databaseInfo 不可为空");
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
            throw new APIException(APIConstants.CODE_PARAM_ERR,"tableName 不可为空");
        }
        String sql = "ALTER TABLE " + oldTableName + " RENAME TO " + newTableName;
        System.out.println("———————————————— alter table XXX rename to YYY ");
        System.out.println(sql);
        return sql;
    }

    /**
     * 查看 表的主键SQL
     *
     * @return
     */
    public static String selectPrimaryKeySql(Long databaseType, String tableName) {
        StringBuilder sb = new StringBuilder();
        // 主键
        if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
            // TODO HIVE表 无主键
        } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
            sb.append("select pg_constraint.conname as pk_name from pg_constraint "+
                    " inner join pg_class on pg_constraint.conrelid = pg_class.oid where pg_class.relname = '"+tableName+"' and pg_constraint.contype='p'");
        } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
            //sb.append("ALTER TABLE " + tableName + " CONSTRAINT " + tableName + "_PK PRIMARY KEY (" + dlRescataColumn.getColumnDefinition() + " )");
        }
        System.out.println("romve primary key of table： " + sb.toString());
        return sb.toString();

    }

    /**
     * 删除 设置表的主键SQL
     *
     * @return
     */
    public static String romvePrimaryKeySql(Long databaseType, String tableName, List<String> primaryKeyList) {
        StringBuilder sb = new StringBuilder();
        for (String primaryKey : primaryKeyList) {
            // 主键
            if (ResourceConstant.DATABASE_TYPE_HIVE.equals(databaseType)) {
                // TODO HIVE表 无主键
            } else if (ResourceConstant.DATABASE_TYPE_MPP.equals(databaseType)) {
                sb.append("ALTER TABLE " + tableName + " DROP CONSTRAINT " + primaryKey);
            } else if (ResourceConstant.DATABASE_TYPE_ORACLE.equals(databaseType)) {
                //sb.append("ALTER TABLE " + tableName + " CONSTRAINT " + tableName + "_PK PRIMARY KEY (" + dlRescataColumn.getColumnDefinition() + " )");
            }
            System.out.println("romve primary key of table： " + sb.toString());
            return sb.toString();
        }
        return null;
    }

    public static boolean isDBField(String fieldName) throws APIException {
        if(!Validator.isMactchRegex("(^_([a-zA-Z0-9]_?)*$)|(^[a-zA-Z](_?[a-zA-Z0-9])*_?$)",fieldName)) {
            return false;
        }

        List<String> sqlKeyWordList= Arrays.asList(DdlSqlUtils.sqlKeyword.split(","));
        if(sqlKeyWordList.contains(fieldName.toUpperCase())){
            return false;
        }

        return true;

    }

    public static void isDBFieldThrowAnException(String fieldName,int fieldLength) throws APIException {

        if(StrUtil.isBlank(fieldName)){
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "字段定义不能为空");
        }
        fieldName=fieldName.trim();
        if(!Validator.isMactchRegex("(^_([a-zA-Z0-9]_?)*$)|(^[a-zA-Z](_?[a-zA-Z0-9])*_?$)",fieldName)) {
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "字段定义只能英文字母开头,可以包含英文、数字和下划线");
        }

        List<String> sqlKeyWordList= Arrays.asList(DdlSqlUtils.sqlKeyword.split(","));
        if(sqlKeyWordList.contains(fieldName.toUpperCase())){
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "字段定义不能使用关键字 "+fieldName);
        }

        if(fieldName.length()>fieldLength){
            throw new APIException(APIConstants.PARAMS_NOT_Valid, "字段定义最多不能超过"+fieldLength+"个字符");
        }

    }

    public static List<String> getDuplicateList(List<String> dataList){
        List<String> duplicateData = new ArrayList<>();
        Map<String, Long> collect = dataList.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        collect.forEach((k,v)->{
            if(v>1)
                duplicateData.add(k);
        });
        return duplicateData;
    }
}
