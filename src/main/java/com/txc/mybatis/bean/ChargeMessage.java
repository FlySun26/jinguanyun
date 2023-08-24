package com.txc.mybatis.bean;

import com.txc.mybatis.util.CRCUtil;
import lombok.Data;

/**
 * @ClassName ChargeMessage
 * @Description TODO
 * @Date 2023/8/24 09:36
 * @Vertion 1.0
 **/
@Data
public class ChargeMessage {

    private Integer num;
    //交易流水号
    private String serialNumber;
    //卡号
    private Integer cardNum;
    //启动方式
    private Integer startType;
    //电压
    private Integer voltage;
    //电流
    private Integer current;
    //正极温度
    private Integer positiveElectrodeTemperature;
    //负极温度
    private Integer negativeElectrodeTemperature;
    //充电时长
    private Integer chargeTime;
    //充电电量
    private Integer chargeDosage;
    //充电费用
    private Integer chargeCost;
    //SOC
    private Integer SOC;
    //需求电压
    private Integer demandVoltage;
    //需求电流
    private Integer demanCdurrent;
    //单体最高温度
    private Integer maxTemperature;
    //单体最低温度
    private Integer minTemperature;
    //单体最高电压
    private Integer maxVoltage;
}
