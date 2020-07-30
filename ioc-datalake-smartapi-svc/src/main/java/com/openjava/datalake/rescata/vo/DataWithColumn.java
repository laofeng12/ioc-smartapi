package com.openjava.datalake.rescata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Author JiaHai
 * @Description 字段名list + 内容List<Object[]>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataWithColumn implements Serializable {
    private static final long serialVersionUID = 6090735946534427739L;

    private List<String> columnNameList;
    private List<Object[]> data;
}
