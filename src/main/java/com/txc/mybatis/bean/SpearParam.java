package com.txc.mybatis.bean;

import lombok.Data;

/**
 * @ClassName HeartbeatParam
 * @Description TODO
 * @Date 2023/8/22 15:08
 * @Vertion 1.0
 **/
@Data
public class SpearParam {

    /**
     * 充电状态
     */
    private Integer chargeStatus;

    /**
     * 充电电压/故障代码/几点
     */
    private Integer param1;


    /**
     * 充电电流/参数1/几分
     */
    private Integer param2;

    /**
     * 充电电量/参数2
     */
    private Integer param3;
}
