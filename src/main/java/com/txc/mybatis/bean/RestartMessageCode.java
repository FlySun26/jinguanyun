package com.txc.mybatis.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RestartMessageCode
 * @Description TODO
 * @Date 2023/9/1 10:59
 * @Vertion 1.0
 **/
@Data
public class RestartMessageCode implements Serializable {

    private Integer restartType;

    private Integer passwordParam;
}
