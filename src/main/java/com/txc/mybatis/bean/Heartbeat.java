package com.txc.mybatis.bean;

import lombok.Data;

import java.util.List;

/**
 * @ClassName Heartbeat
 * @Description TODO
 * @Date 2023/8/22 14:19
 * @Vertion 1.0
 **/
@Data
public class Heartbeat {


    /**
     * 充电桩编号
     */
    private Integer devAddr;

    /**
     * 充电枪数量
     */
    private Integer spearNum;


    /**
     * 充电枪状态列表
     */
    private List<SpearParam> spearParamList;

}
