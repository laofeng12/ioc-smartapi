package com.openjava.datalake.util;

import oracle.sql.CLOB;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Clob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * @Author xjd
 * @Date 2020/3/21 16:15
 * @Version 1.0
 */
public class DatabaseDataTypeTransferUtils {

    public static String oracleTimestampToString(Object value) {
        Timestamp timestamp = null;
        try {
            if (value instanceof oracle.sql.TIMESTAMP) {
                Class clz = value.getClass();
                Method m = clz.getMethod("timestampValue", null);
                timestamp = (Timestamp) m.invoke(value, null);
            } else {
                timestamp = (Timestamp) value;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        if (timestamp!=null) {
            return (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S")) .format(timestamp);
        } else {
            return null;
        }
    }
    public static Timestamp toJdbcTimestamp(Object value) {
        Timestamp timestamp = null;
        try {
            if (value instanceof oracle.sql.TIMESTAMP) {
                Class clz = value.getClass();
                Method m = clz.getMethod("timestampValue", null);
                timestamp = (Timestamp) m.invoke(value, null);
            } else {
                timestamp = (Timestamp) value;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    public static String clobToString(Object value) {
        String res="";
        if (value instanceof CLOB) {
            CLOB clob = (CLOB) value;
            Reader io = null;
            try {
                if(clob ==null || clob.getCharacterStream() ==null){

                }else{
                    io=clob.getCharacterStream();
                    BufferedReader br = new BufferedReader(io);
                    char[] tempDoc = new char[(int) clob.length()];
                    br.read(tempDoc);
                    res = new String(tempDoc);
//                    String s = br.readLine();
//                    StringBuffer sb = new StringBuffer();
//                    while (s !=null ) {
//                        sb.append(s);
//                        s=br.readLine();
//                    }
//                    res=sb.toString();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (io != null) {
                        io.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (value instanceof Clob) {
            Clob clob = (Clob) value;
            Reader inStreamDoc = null;
            try {
                inStreamDoc = clob.getCharacterStream();
                char[] tempDoc = new char[(int) clob.length()];
                inStreamDoc.read(tempDoc);
                res = new String(tempDoc);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException es) {
                es.printStackTrace();
            } finally {
                try {
                    if (inStreamDoc != null) {
                        inStreamDoc.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return res;
    }
}
