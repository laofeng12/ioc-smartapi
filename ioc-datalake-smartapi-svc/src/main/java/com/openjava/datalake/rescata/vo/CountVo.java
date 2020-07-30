package com.openjava.datalake.rescata.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 分组统计VO
 *
 * @author Jiahai
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CountVo implements Serializable {
    private static final long serialVersionUID = 172765596442855326L;

    /**
     * key
     */
    private Object key;

    /**
     * 数量
     */
    private Long amount;
}
