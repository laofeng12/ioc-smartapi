package com.openjava.datalake.dataxjdbcutil.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.openjava.datalake.common.*;
import com.openjava.datalake.dataxjdbcutil.exception.DBUtilErrorCode;
import com.openjava.datalake.dataxjdbcutil.exception.DataXException;
import com.openjava.datalake.dataxjdbcutil.model.ColumnInfo;
import com.openjava.datalake.dataxjdbcutil.model.TableContext;
import com.openjava.datalake.util.*;
import com.openjava.mq.component.rabbitmq.common.SpringContextUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.ljdp.component.exception.APIException;
import org.ljdp.component.result.APIConstants;
import org.openjava.boot.conf.DtsIntegrationConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author xjd
 * @Date 2019/9/2 17:19
 * @Version 1.0
 */
public class DataLakeWriter {

    protected static final Logger LOG = LoggerFactory.getLogger(DataLakeWriter.class);

    private static final String VALUE_HOLDER = "?";
    private static final String PASSWORD = "PWD#ioc201912";
    protected String writeRecordSql;
    protected String writeDataSqlTemplate;
    protected String tableName;
    protected int columnNumber = 0;
    protected List<String> columns;
    private Integer pkIndex = null;
    private String pkColumnName = null;
    private TableContext tableContext = null;

    public DataLakeWriter(TableContext buffer) {
    	this.tableContext = buffer;
        //设置额外的字段
    	if(!PublicConstant.EXCEL_PROCESS_TYPE_DELETE.equals(tableContext.getOperType())) {
    		setExtraFields(buffer); 
    	}
        List<ColumnInfo> columnInfos = buffer.getColumnInfos();
        List<String> list = new ArrayList<>();
        for (int i = 0; i < columnInfos.size(); i++) {
            ColumnInfo columnInfo = columnInfos.get(i);
            String columnName = columnInfo.getColumnName();
            Long isPrimaryKey = columnInfo.getIsPrimaryKey();
            if (1L == isPrimaryKey) {
                this.pkIndex = i;
            }
            list.add(columnName);
        }
        this.columns = list;
        this.columnNumber = columnInfos.size();
        this.tableName = buffer.getTableName();
        String operType = buffer.getOperType();
        pkColumnName = buffer.getPkColumnName();
        if (PublicConstant.EXCEL_PROCESS_TYPE_UPDATE_OR_INSERT.equals(operType) && buffer.getNeedUpdate()) {
            this.calcUpdateRecordSql(pkColumnName);
        } else if (PublicConstant.EXCEL_PROCESS_TYPE_DELETE.equals(operType)) {
            this.calcDeleteRecordSql();
        } else {
            this.calcInsertRecordSql();
        }
    }


    /**
     * 设置额外的7个字段属性信息和字段值
     * @param buffer
     */
    private void setExtraFields(TableContext buffer) {
        if (null == buffer) {
            return;
        }
        //设置7个字段的值
        setDataRows(buffer);
        //设置7个字段的字段属性信息
        setColumnInfos(buffer);
    }

    private void setDataRows(TableContext buffer) {
        //获取字段的值集合
        List<? extends Object[]> dataRows = buffer.getDataRows();
        //dataRows为null || dataRows长度为0 || dataRows.get(0)的数组长度为0
        if (CollectionUtils.isEmpty(dataRows) || dataRows.get(0).length == 0) {
            return;
        }
        //获取seq值
        ParameterizedTypeReference<Long> typeReference = new ParameterizedTypeReference<Long>() {};
        Map<String, String> getmultiValueMap = new HashMap<>();
        getmultiValueMap.put("tableName", buffer.getTableName());
        JSONObject getJObject = JSON.parseObject(JSON.toJSONString(getmultiValueMap));
        DtsIntegrationConfig dtsIntegrationConfig = SpringContextUtil.getBean(DtsIntegrationConfig.class);
        Long seq = RestTemplateUtils.postJsonBody(dtsIntegrationConfig.getQueryDoSeqNumber(), typeReference, getJObject);

        List<Object[]> list = new ArrayList<>();
        //遍历dataRows=>一个dataRow对应一条数据库记录
        for (Object[] dataRow : dataRows) {
            //创建数组(原本的字段内容+额外的7个字段内容)
            Object[] row = new Object[dataRow.length + 7];
            //赋值原本的字段内容
            for (int i = 0; i < dataRow.length; i++) {
                row[i] = dataRow[i];
            }
            //赋值额外的7个字段内容
            //1.ID=>ID 为日期(8位)+时间(6位)+SEQ值（12位）每个表一个SEQ值 ，SEQ_表名  20191217091501000000000001   同表名的就用同一个SEQ值
            row[dataRow.length -1 + 1] = SeqUtils.getSeqStrByNumber(seq++);

            //2.目录ID=>根据“目录清单表”的“目录ID”自动生成(现在为空)
            row[dataRow.length -1 + 2] = "";

            //3.增加时间
            row[dataRow.length -1 + 3] = DateUtils.getDateTime();

            //4.更新时间
            row[dataRow.length -1 + 4] = DateUtils.getDateTime();

            //5.状态=>状态用8位0或者1表示，第2位是1正常；第3位1做废 ；第4位1垃圾数据  应用加载时只加载第2位是1正常的数据其它位数据随机生成(现在默认使用1)
            row[dataRow.length -1 + 5] = "1";

            //7.防窜改码时间
            Date date = new Date();
            String dateStr = DateUtils.getDate(date,DateUtils.DATETIME_FORMAT);
            row[dataRow.length -1 + 7] =dateStr;

            //6.防窜改码=>md5（全部字段内容值(非字段名称)+防篡改时间+PWD#ioc201912）
            row[dataRow.length -1 + 6] = getHashTokimnhjStr(dataRow,date);
            //添加到集合
            list.add(row);
        }
        buffer.setDataRows(list);

        //更新seq值
        ParameterizedTypeReference<String> updateTypeReference = new ParameterizedTypeReference<String>() {};
        Map<String, Object> updateMultiValueMap = new HashMap<>();
        updateMultiValueMap.put("tableName", buffer.getTableName());
        updateMultiValueMap.put("seq", seq);
        JSONObject updateJObject = JSON.parseObject(JSON.toJSONString(updateMultiValueMap));
        RestTemplateUtils.postJsonBody(dtsIntegrationConfig.getUpdateDoSeqNumber(), updateTypeReference, updateJObject);
    }

    private String getHashTokimnhjStr(Object[] dataRow,Date date) {
        List<Object> objects = new ArrayList<>();
        //新需求,不需要前面得字段,直接用时间+PASSWORD
        /*for (Object o : dataRow) {
            objects.add(o);
        }*/
        objects.add(date);
        objects.add(PASSWORD);
        return SeqUtils.hashTokimnhj(objects);
    }

    private void setColumnInfos(TableContext buffer) {
        //获取字段集合
        List<ColumnInfo> columnInfos=buffer.getColumnInfos();
        if (CollectionUtils.isEmpty(columnInfos)) {
            return;
        }
        //ID
        ColumnInfo columnInfo1 = new ColumnInfo();
        columnInfo1.setColumnSqltype(Types.VARCHAR);
        columnInfo1.setColumnName(ResourceExtraFieldsConstant.ID_TOKIMNHJ);
        columnInfo1.setColumnComment(ResourceExtraFieldsConstant.ID_TOKIMNHJ_COMMENT);
        columnInfo1.setDataType(ResourceConstant.COLUMN_DATATYPE_LONG_STRING);
        columnInfo1.setColumnLength(400L);
        columnInfo1.setIsPrimaryKey(PublicConstant.NO);
        columnInfo1.setIsNullable(PublicConstant.NO);
        //目录ID
        ColumnInfo columnInfo2 = new ColumnInfo();
        columnInfo2.setColumnSqltype(Types.VARCHAR);
        columnInfo2.setColumnName(ResourceExtraFieldsConstant.DIR_ID_TOKIMNHJ);
        columnInfo2.setColumnComment(ResourceExtraFieldsConstant.DIR_ID_TOKIMNHJ_COMMENT);
        columnInfo2.setDataType(ResourceConstant.COLUMN_DATATYPE_LONG_STRING);
        columnInfo2.setColumnLength(100L);
        columnInfo2.setIsPrimaryKey(PublicConstant.NO);
        columnInfo2.setIsNullable(PublicConstant.YES);
        //增加时间
        ColumnInfo columnInfo3 = new ColumnInfo();
        columnInfo3.setColumnSqltype(Types.TIMESTAMP);
        columnInfo3.setColumnName(ResourceExtraFieldsConstant.ADDTIME_TOKIMNHJ);
        columnInfo3.setColumnComment(ResourceExtraFieldsConstant.ADDTIME_TOKIMNHJ_COMMENT);
        columnInfo3.setDataType(ResourceConstant.COLUMN_DATATYPE_DATE);
        columnInfo3.setDataFormat(DateUtils.DATETIME_FORMAT);
        columnInfo3.setIsPrimaryKey(PublicConstant.NO);
        columnInfo3.setIsNullable(PublicConstant.YES);
        //更新时间
        ColumnInfo columnInfo4 = new ColumnInfo();
        columnInfo4.setColumnSqltype(Types.TIMESTAMP);
        columnInfo4.setColumnName(ResourceExtraFieldsConstant.UPDATETIME_TOKIMNHJ);
        columnInfo4.setColumnComment(ResourceExtraFieldsConstant.UPDATETIME_TOKIMNHJ_COMMENT);
        columnInfo4.setDataType(ResourceConstant.COLUMN_DATATYPE_DATE);
        columnInfo4.setDataFormat(DateUtils.DATETIME_FORMAT);
        columnInfo4.setIsPrimaryKey(PublicConstant.NO);
        columnInfo4.setIsNullable(PublicConstant.YES);
        //状态
        ColumnInfo columnInfo5 = new ColumnInfo();
        columnInfo5.setColumnSqltype(Types.VARCHAR);
        columnInfo5.setColumnName(ResourceExtraFieldsConstant.STATE_TOKIMNHJ);
        columnInfo5.setColumnComment(ResourceExtraFieldsConstant.STATE_TOKIMNHJ_COMMENT);
        columnInfo5.setDataType(ResourceConstant.COLUMN_DATATYPE_LONG_STRING);
        columnInfo5.setColumnLength(50L);
        columnInfo5.setIsPrimaryKey(PublicConstant.NO);
        columnInfo5.setIsNullable(PublicConstant.NO);
        //防窜改码
        ColumnInfo columnInfo6 = new ColumnInfo();
        columnInfo6.setColumnSqltype(Types.VARCHAR);
        columnInfo6.setColumnName(ResourceExtraFieldsConstant.HASH_TOKIMNHJ);
        columnInfo6.setColumnComment(ResourceExtraFieldsConstant.HASH_TOKIMNHJ_COMMENT);
        columnInfo6.setDataType(ResourceConstant.COLUMN_DATATYPE_LONG_STRING);
        columnInfo6.setColumnLength(500L);
        columnInfo6.setIsPrimaryKey(PublicConstant.NO);
        columnInfo6.setIsNullable(PublicConstant.NO);
        //防窜改码时间
        ColumnInfo columnInfo7 = new ColumnInfo();
        columnInfo7.setColumnSqltype(Types.TIMESTAMP);
        columnInfo7.setColumnName(ResourceExtraFieldsConstant.DATA_HASH_TOKIMNHJ);
        columnInfo7.setColumnComment(ResourceExtraFieldsConstant.DATA_HASH_TOKIMNHJ_COMMENT);
        columnInfo7.setDataType(ResourceConstant.COLUMN_DATATYPE_DATE);
        columnInfo7.setDataFormat(DateUtils.DATETIME_FORMAT);
        columnInfo7.setIsPrimaryKey(PublicConstant.NO);
        columnInfo7.setIsNullable(PublicConstant.YES);

        //添加到集合
        columnInfos.add(columnInfo1);
        columnInfos.add(columnInfo2);
        columnInfos.add(columnInfo3);
        columnInfos.add(columnInfo4);
        columnInfos.add(columnInfo5);
        columnInfos.add(columnInfo6);
        columnInfos.add(columnInfo7);
        buffer.setColumnInfos(columnInfos);
    }

    public int doBatchInsert(Connection connection, TableContext buffer)
            throws SQLException, APIException {
        int executeCount;
        PreparedStatement preparedStatement = null;
        try {
            // 事务开始
            connection.setAutoCommit(false);

            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);

            List<ColumnInfo> columnInfos = buffer.getColumnInfos();
            List<? extends Object[]> dataRows = buffer.getDataRows();
            for (int i = 0; i < dataRows.size(); i++) {
                Object[] row = dataRows.get(i);
                preparedStatement = fillPreparedStatement(preparedStatement, columnInfos, row);
                preparedStatement.addBatch();
            }
            // 是否全量更新
            if (PublicConstant.EXCEL_PROCESS_TYPE_DELETEALL_AND_INSERT.equals(buffer.getOperType())) {
                DbUtil.deleteAllFromTableWithoutCloseConn(connection, buffer.getTableName());
            }
            // 执行插入
            int[] ints = preparedStatement.executeBatch();
            executeCount = Arrays.stream(ints).sum();
            // 提交事务
            connection.commit();
        } catch (SQLException e) {
            LOG.warn("回滚此次写入, 因为:" + e.getMessage());
            connection.rollback();
            throw new APIException(APIConstants.CODE_PARAM_ERR, e.getMessage());
//            doOneInsert(connection, buffer);
        } catch (DataXException e){
            throw new APIException(APIConstants.CODE_PARAM_ERR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_PARAM_ERR, "导入数据错误,错误信息：" + e.getMessage());
        } finally {
            // 最后不要忘了关闭 preparedStatement
//            System.out.println("关闭statement："+preparedStatement.toString());
            preparedStatement.clearParameters();
            org.springframework.jdbc.support.JdbcUtils.closeStatement(preparedStatement);
//            System.out.println("关闭connection："+connection.toString());
            org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);
        }
        return executeCount;
    }

    /**
     * 批量更新 add by zmk
     * 全部执行成功才提交事务
     */
    public int doUpdateBatchExecute(Connection connection, TableContext buffer)throws APIException{
        pkColumnName = buffer.getPkColumnName();
        this.calcUpdateRecordSql(pkColumnName);
        int executeCount;
        PreparedStatement preparedStatement = null;
        try {
            // 事务开始
            connection.setAutoCommit(false);

            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);

            List<ColumnInfo> columnInfos = buffer.getColumnInfos();
            List<? extends Object[]> dataRows = buffer.getDataRows();
            for (int i = 0; i < dataRows.size(); i++) {
                Object[] row = dataRows.get(i);
                preparedStatement = fillPreparedStatement(preparedStatement, columnInfos, row);
                preparedStatement.addBatch();
            }

            // 执行更新
            int[] ints = preparedStatement.executeBatch();

            executeCount = Arrays.stream(ints).sum();
            if (executeCount<dataRows.size()){
                return executeCount;
            }{
                connection.commit(); // 提交事务
            }
        } catch (SQLException e) {
            LOG.warn("回滚此次写入, 因为:" + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                LOG.error("回滚数据异常, 因为:" + e.getMessage());
                throw new APIException(APIConstants.CODE_PARAM_ERR, "回滚数据异常, 因为:" + e.getMessage());
            }
            throw new APIException(APIConstants.CODE_PARAM_ERR, "回滚此次写入, 因为:" + e.getMessage());
//            doOneInsert(connection, buffer);
        } catch (DataXException e){
            throw new APIException(APIConstants.CODE_PARAM_ERR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_PARAM_ERR, "导入数据错误,错误信息：" + e.getMessage());
        } finally {
            // 最后不要忘了关闭 preparedStatement
//            System.out.println("关闭statement："+preparedStatement.toString());
            try {
                preparedStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("preparedStatement清理异常, 因为:" + e.getMessage());
                throw new APIException(APIConstants.CODE_PARAM_ERR, "preparedStatement清理异常, 因为:" + e.getMessage());

            }
            org.springframework.jdbc.support.JdbcUtils.closeStatement(preparedStatement);
//            System.out.println("关闭connection："+connection.toString());
//            org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);
        }
        return executeCount;
    }

    /**
     * 批量更新 add by zmk
     * 全部执行成功才提交事务
     */
    public int doDeleteBatchExecute(Connection connection, TableContext buffer)throws APIException{
        this.calcDeleteRecordSql();
        int executeCount;
        PreparedStatement preparedStatement = null;
        try {
            // 事务开始
            connection.setAutoCommit(false);

            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);

            List<ColumnInfo> columnInfos = buffer.getColumnInfos();
            List<? extends Object[]> dataRows = buffer.getDataRows();
            for (int i = 0; i < dataRows.size(); i++) {
                Object[] row = dataRows.get(i);
                preparedStatement = fillPreparedStatement(preparedStatement, columnInfos, row);
                preparedStatement.addBatch();
            }

            // 执行更新
            int[] ints = preparedStatement.executeBatch();

            executeCount = Arrays.stream(ints).sum();
            if (executeCount<dataRows.size()){
                return executeCount;
            }{
                connection.commit(); // 提交事务
            }
        } catch (SQLException e) {
            LOG.warn("回滚此次写入, 因为:" + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                LOG.error("回滚数据异常, 因为:" + e.getMessage());
                throw new APIException(APIConstants.CODE_PARAM_ERR, "回滚数据异常, 因为:" + e.getMessage());
            }
            throw new APIException(APIConstants.CODE_PARAM_ERR, "回滚此次写入, 因为:" + e.getMessage());
//            doOneInsert(connection, buffer);
        } catch (DataXException e){
            throw new APIException(APIConstants.CODE_PARAM_ERR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_PARAM_ERR, "导入数据错误,错误信息：" + e.getMessage());
        } finally {
            // 最后不要忘了关闭 preparedStatement
//            System.out.println("关闭statement："+preparedStatement.toString());
            try {
                preparedStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("preparedStatement清理异常, 因为:" + e.getMessage());
                throw new APIException(APIConstants.CODE_PARAM_ERR, "preparedStatement清理异常, 因为:" + e.getMessage());

            }
            org.springframework.jdbc.support.JdbcUtils.closeStatement(preparedStatement);
//            System.out.println("关闭connection："+connection.toString());
//            org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);
        }
        return executeCount;
    }

    /**
     * 异步批量执行插入或更新，全量删除不写在里面
     * @param connection
     * @param buffer
     * @return
     * @throws SQLException
     * @throws APIException
     */
    public int doSyncBatchExecute(Connection connection, TableContext buffer)
            throws APIException {
        int executeCount;
        PreparedStatement preparedStatement = null;
        try {
            // 事务开始
            connection.setAutoCommit(false);

            preparedStatement = connection
                    .prepareStatement(this.writeRecordSql);
            
            List<ColumnInfo> columnInfos = buffer.getColumnInfos();
            LOG.info("columnInfos====={}",JSONObject.toJSONString(columnInfos));
            Boolean isUpdate = this.tableContext.getNeedUpdate();
            LOG.info("isUpdate=={}, operType=={}",isUpdate,buffer.getOperType());
            //数据更新时将主键列移到最后
            if(isUpdate != null && isUpdate) {
            	Collections.rotate(columnInfos, -1);
            	LOG.info("columnInfosSort=={}",JSONObject.toJSONString(columnInfos));
            }
            List<? extends Object[]> dataRows = buffer.getDataRows();
            for (int i = 0; i < dataRows.size(); i++) {
                Object[] row = dataRows.get(i);
                //数据更新时将主键值移到最后
                if(isUpdate != null && isUpdate) {
                	List<Object> objs = Arrays.asList(row);
                	Collections.rotate(objs, -1);
                	row = objs.toArray(new Object[objs.size()]);
                	LOG.info("rowSort=={}",JSONObject.toJSONString(row));
                }
                preparedStatement = fillPreparedStatement(preparedStatement, columnInfos, row);
                preparedStatement.addBatch();
            }

            // 执行插入
            int[] ints = preparedStatement.executeBatch();

            executeCount = Arrays.stream(ints).sum();
            // 提交事务
            connection.commit();
        } catch (SQLException e) {
            LOG.warn("回滚此次写入, 因为:" + e.getMessage());
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
                LOG.error("回滚数据异常, 因为:" + e.getMessage());
                throw new APIException(APIConstants.CODE_PARAM_ERR, "回滚数据异常, 因为:" + e.getMessage());
            }
            throw new APIException(APIConstants.CODE_PARAM_ERR, "回滚此次写入, 因为:" + e.getMessage());
//            doOneInsert(connection, buffer);
        } catch (DataXException e){
            throw new APIException(APIConstants.CODE_PARAM_ERR, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_PARAM_ERR, "导入数据错误,错误信息：" + e.getMessage());
        } finally {
            // 最后不要忘了关闭 preparedStatement
//            System.out.println("关闭statement："+preparedStatement.toString());
            try {
                preparedStatement.clearParameters();
            } catch (SQLException e) {
                e.printStackTrace();
                LOG.error("preparedStatement清理异常, 因为:" + e.getMessage());
                throw new APIException(APIConstants.CODE_PARAM_ERR, "preparedStatement清理异常, 因为:" + e.getMessage());

            }
            org.springframework.jdbc.support.JdbcUtils.closeStatement(preparedStatement);
//            System.out.println("关闭connection："+connection.toString());
//            org.springframework.jdbc.support.JdbcUtils.closeConnection(connection);
        }
        return executeCount;
    }

    public int doOneInsert(Connection connection, TableContext buffer) throws APIException {
        PreparedStatement preparedStatement = null;
        int executeCount = 0;
        try {
            connection.setAutoCommit(true);
            preparedStatement = connection.prepareStatement(this.writeRecordSql);
            List<ColumnInfo> columnInfos = buffer.getColumnInfos();
            List<? extends Object[]> dataRows = buffer.getDataRows();
            // 正常只有一条，为了适配buffer里面list，没去掉for
            for (int i = 0; i < dataRows.size(); i++) {
                Object[] rows = dataRows.get(i);
                try {
                    preparedStatement = fillPreparedStatement(preparedStatement, columnInfos, rows);
                    String operType = buffer.getOperType();
                    if (PublicConstant.EXCEL_PROCESS_TYPE_UPDATE_OR_INSERT.equals(operType) || PublicConstant.EXCEL_PROCESS_TYPE_DELETE.equals(operType)) {
                        int count = preparedStatement.executeUpdate();
                        executeCount = executeCount + count;
                    } else {
                        boolean execute = preparedStatement.execute();
                        executeCount++;
                    }
                } catch (SQLException e) {
                    LOG.debug(e.toString());
                    throw new APIException(APIConstants.CODE_PARAM_ERR, "导入数据错误,错误信息：" + e.getMessage());
                } finally {
                    // 最后不要忘了关闭 preparedStatement
                    preparedStatement.clearParameters();
                }
            }
            return executeCount;
        } catch (Exception e) {
            e.printStackTrace();
            throw new APIException(APIConstants.CODE_PARAM_ERR, "导入数据错误,错误信息：" + e.getMessage());
        } finally {
            JdbcUtils.release(null, preparedStatement, null);
        }
    }

    // 直接使用了两个类变量：columnNumber,resultSetMetaData

    public PreparedStatement fillPreparedStatement(PreparedStatement preparedStatement, List<ColumnInfo> columnInfos, Object[] rows) throws SQLException, DataXException {
        for (int i = 0; i < columnInfos.size(); i++) {
            preparedStatement = fillPreparedStatementColumnType(preparedStatement, i, columnInfos.get(i), rows[i]);
        }
        return preparedStatement;
    }
    /**
     * 填充一个占位符，根据字段信息，设置不同的类型，占位符的位置用字段的位置算
     * @param preparedStatement 待填充的sql
     * @param columnIndex 要填充的位置
     * @param columnInfo 字段属性信息
     * @param dataValue 要填充的value值
     * @return
     * @throws SQLException
     * @throws DataXException
     */
    public PreparedStatement fillPreparedStatementColumnType(PreparedStatement preparedStatement, int columnIndex, ColumnInfo columnInfo, Object dataValue) throws SQLException, DataXException {
//        SimpleDateFormat millisecondSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//        SimpleDateFormat mmddyySdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date utilDate;
        int columnSqltype = columnInfo.getColumnSqltype();
        switch (columnSqltype) {
            case Types.CHAR:
            case Types.NCHAR:
            case Types.CLOB:
            case Types.NCLOB:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.LONGNVARCHAR:
                preparedStatement.setString(columnIndex + 1, dataValue == null ? null : dataValue.toString());
                break;

            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
                Long longValue = null;
                BigInteger bigInteger = null;

                if (dataValue != null && !"".equals(dataValue.toString().trim())) {
                    bigInteger = new BigInteger(dataValue.toString());
                }
                if (null != bigInteger) {
                    OverFlowUtil.validateLongNotOverFlow(bigInteger);
                    longValue =  bigInteger.longValue();
                }
                if (null == longValue) {
                    preparedStatement.setNull(columnIndex + 1, columnSqltype);
                } else {
                    preparedStatement.setLong(columnIndex + 1, longValue);
                }
                break;
            case Types.NUMERIC:
            case Types.DECIMAL:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
                Double decimalValue = null;
                BigDecimal bigDecimal = null;

                if (dataValue != null && !"".equals(dataValue.toString().trim())) {
                    bigDecimal = new BigDecimal(dataValue.toString());
                }
                if (null != bigDecimal) {
                    OverFlowUtil.validateDoubleNotOverFlow(bigDecimal);
                    decimalValue =  bigDecimal.doubleValue();
                }
                if (null == decimalValue) {
                    preparedStatement.setNull(columnIndex + 1, columnSqltype);
                } else {
                    preparedStatement.setDouble(columnIndex + 1, decimalValue);
                }
                break;

            //tinyint is a little special in some database like mysql {boolean->tinyint(1)}
            case Types.TINYINT:
                Long longValue1 = null;
                BigInteger rawData1 = null;
                if (dataValue != null) {
                    rawData1 = new BigInteger(dataValue.toString());
                }
                if (null != rawData1) {
                    OverFlowUtil.validateLongNotOverFlow(rawData1);
                    longValue1 =  rawData1.longValue();
                }
                if (null == longValue1) {
                    preparedStatement.setNull(columnIndex + 1, columnSqltype);
                } else {
                    preparedStatement.setLong(columnIndex + 1, longValue1);
                }
                break;

            // for mysql bug, see http://bugs.mysql.com/bug.php?id=35115
            case Types.DATE:
                java.sql.Date sqlDate = null;
//                java.sql.Timestamp sqlDateToTimestamp = null;
                if (dataValue != null && StringUtils.isNotBlank(dataValue.toString())) {
                    utilDate = MyDateFormater.praseDate(dataValue.toString());
//                    if (utilDate == null) {
//                        try {
//                            utilDate = millisecondSdf.parse(dataValue.toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                        try {
//                            utilDate = mmddyySdf.parse(dataValue.toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    if (utilDate == null) {
                        throw DataXException
                                .asDataXException(
                                        DBUtilErrorCode.UNSUPPORTED_TYPE,
                                        String.format("日期字段格式不正确，字段名[%s],字段值[%s]", columnInfo.getColumnName(), dataValue.toString()));
                    }
                } else {
                    utilDate = null;
                }
                if (null != utilDate) {
                    sqlDate = new java.sql.Date(utilDate.getTime());
//                    sqlDateToTimestamp = new java.sql.Timestamp(utilDate.getTime());
                }
                preparedStatement.setDate(columnIndex + 1, sqlDate);
//                preparedStatement.setTimestamp(columnIndex + 1, sqlDateToTimestamp);
                break;

            case Types.TIME:
                java.sql.Time sqlTime = null;
                if (dataValue != null && StringUtils.isNotBlank(dataValue.toString())) {
                    utilDate = MyDateFormater.praseDate(dataValue.toString());
//                    utilDate = DateFormater.praseDate(dataValue.toString());
//                    if (utilDate == null) {
//                        try {
//                            utilDate = millisecondSdf.parse(dataValue.toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    if (utilDate == null) {
                        throw DataXException
                                .asDataXException(
                                        DBUtilErrorCode.UNSUPPORTED_TYPE,
                                        String.format("日期字段格式不正确，字段名[%s],字段值[%s]", columnInfo.getColumnName(), dataValue.toString()));
                    }
                } else {
                    utilDate = null;
                }
                if (null != utilDate) {
                    sqlTime = new java.sql.Time(utilDate.getTime());
                }
                preparedStatement.setTime(columnIndex + 1, sqlTime);
                break;

            case Types.TIMESTAMP:
                java.sql.Timestamp sqlTimestamp = null;
                if (dataValue != null && StringUtils.isNotBlank(dataValue.toString())) {
                    utilDate = MyDateFormater.praseDate(dataValue.toString());
//                    utilDate = DateFormater.praseDate(dataValue.toString());
//                    if (utilDate == null) {
//                        try {
//                            utilDate = millisecondSdf.parse(dataValue.toString());
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    if (utilDate == null) {
                        throw DataXException
                                .asDataXException(
                                        DBUtilErrorCode.UNSUPPORTED_TYPE,
                                        String.format("日期字段格式不正确，字段名[%s],字段值[%s]", columnInfo.getColumnName(), dataValue.toString()));
                    }
                } else {
                    utilDate = null;
                }
                if (null != utilDate) {
                    sqlTimestamp = new java.sql.Timestamp(
                            utilDate.getTime());
                }
                preparedStatement.setTimestamp(columnIndex + 1, sqlTimestamp);
                break;

            case Types.BINARY:
            case Types.VARBINARY:
            case Types.BLOB:
            case Types.LONGVARBINARY:
                preparedStatement.setBytes(columnIndex + 1, (byte[]) dataValue);
                break;

            case Types.BOOLEAN:
                Boolean booleanDataValue = (Boolean) dataValue;
                preparedStatement.setString(columnIndex + 1, booleanDataValue ? "true" : "false");
                break;

            // warn: bit(1) -> Types.BIT 可使用setBoolean
            // warn: bit(>1) -> Types.VARBINARY 可使用setBytes
            case Types.BIT:
                String strValueBit = dataValue == null ? null : dataValue.toString();
                preparedStatement.setString(columnIndex + 1, strValueBit);
                break;
            default:
                throw DataXException
                        .asDataXException(
                                DBUtilErrorCode.UNSUPPORTED_TYPE,
                                String.format(
                                        "您的配置文件中的列配置信息有误. 因为DataX 不支持数据库写入这种字段类型. 字段名:[%s], 字段类型:[%d], 字段Java类型:[%s]. 请修改表中该字段的类型或者不同步该字段.",
                                        columnInfo.getColumnName(),
                                        columnInfo.getColumnSqltype(),
                                        columnInfo.getColumnSqltype()));
        }
        System.out.println("preparedStatement"+preparedStatement);
        return preparedStatement;
    }

    private void calcInsertRecordSql() {
        List<String> valueHolders = new ArrayList<String>(columnNumber);
        for (int i = 0; i < columns.size(); i++) {
            valueHolders.add(calcValueHolder(null));
        }
        writeDataSqlTemplate = new StringBuilder().append("INSERT")
                .append(" INTO %s (").append(StringUtils.join(columns, ","))
                .append(") VALUES(").append(StringUtils.join(valueHolders, ","))
                .append(")").toString();
        writeRecordSql = String.format(writeDataSqlTemplate, tableName);
    }

    private void calcUpdateRecordSql(String pkColumnName) {
        List<String> updateColumns = columns.stream().filter(item -> !item.equals(pkColumnName)).collect(Collectors.toList());
        List<String> setValueList = Lists.newArrayList();
        for (String column : updateColumns) {
            setValueList.add(new StringBuilder()
                        .append(column).append("=")
                        .append(calcValueHolder(null))
                        .toString());
        }
        writeDataSqlTemplate = new StringBuilder().append("UPDATE %s ").append(" SET ")
                .append(StringUtils.join(setValueList, ","))
                .append(" where %s = ").append(calcValueHolder(null))
                .toString();
        writeRecordSql = String.format(writeDataSqlTemplate, tableName, pkColumnName);
    }

    private void calcDeleteRecordSql() {
        writeDataSqlTemplate = new StringBuilder().append("DELETE FROM %s ")
                								  .append("where ").append(pkColumnName)
                								  .append("=").append(calcValueHolder(null))
                                                  .toString();
        writeRecordSql = String.format(writeDataSqlTemplate, tableName);

    }

    public String calcValueHolder(String columnType) {
        return VALUE_HOLDER;
    }


    public static void main(String[] args) {
		List<String> list = Lists.newArrayList();
		list.add("a");
		list.add("b");
		list.add("c");
		Collections.rotate(list, -1);
		System.out.println(list.get(3));
	}

}
