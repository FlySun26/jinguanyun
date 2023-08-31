package com.txc.mybatis.bean;

import com.txc.mybatis.util.CRCUtil;
import lombok.Data;

/**
 * @ClassName BMSStatus
 * @Description TODO
 * @Date 2023/8/25 14:35
 * @Vertion 1.0
 **/
@Data
public class BMSStatus {

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
    //BMS版本
    private Integer BMSType;
    //电池类型
    private Integer batteryType;
    //额定容量
    private Integer ratedCapacity;
    //额定总电压
    private Integer RatedTotalVoltage;
    //BMS生产厂商
    private Integer BMSManufacturer;
    //电池组序号
    private Integer batteryGroupNum;
    //电池组生产日期年
    private Integer batteryGroupManufactureDateYear;
    //电池组生产日期月
    private Integer batteryGroupManufactureDateMonth;
    //电池组生产日期日
    private Integer batteryGroupManufactureDateDay;
    //电池组充电次数
    private Integer batteryGroupChargingCycles;
    //电池电压
    private Integer batteryVoltage;
    //允许温度
    private Integer permissiveTemperature;
    //VIN
    private String VIN;
    //开始SOC
    private Integer startSOC;
    //绝缘检测电压
    private Integer insulationDetectionVoltage;
    //正级阻值
    private Integer positiveResistanceValue;
    //负极阻值
    private Integer negativeResistanceValue;
    //开始时间
    private String startTime;
    //电表度数
    private Integer electricityMeter;
}
