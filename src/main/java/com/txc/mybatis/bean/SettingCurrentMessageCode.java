package com.txc.mybatis.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SettingCurrentMessageCode
 * @Description TODO
 * @Date 2023/8/31 14:04
 * @Vertion 1.0
 **/
@Data
public class SettingCurrentMessageCode implements Serializable {

    //枪编号
    private Integer spearNum;

    //电流参数
    private Integer currentParam;
}
