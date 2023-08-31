package com.txc.mybatis.bean;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName StartMessageCodeMessage
 * @Description TODO
 * @Date 2023/8/29 08:14
 * @Vertion 1.0
 **/
@Data
public class StartMessageCodeMessage implements Serializable {

    //枪编号
    private Integer spearNum;
    //交易流水号
    private String transactionSerialNum;
    //卡号
    private Integer cardNum;
    //卡内余额
    private Integer cardBalance;
    //启动方式
    private Integer startType;

    //BMS辅助电源电压
    private Integer BMSVoltage;
    //SOC设定值
    private Integer SOCValue;
    //金额设定值
    private Integer amountValue;
}
