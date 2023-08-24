package com.txc.mybatis.bean;

import lombok.Data;

/**
 * @ClassName HostStatusMessage
 * @Description TODO
 * @Date 2023/8/24 10:42
 * @Vertion 1.0
 **/
@Data
public class HostStatusMessage {

    //主机进风温度
    private short hostInletTemperature;
    //主机出风温度
    private short hostOutletTemperature;



    //模块1状态
    private byte model1Status;
    //模块1温度
    private short model1Temperature;
    //模块2状态
    private byte model2Status;
    //模块2温度
    private short model2Temperature;
    //模块3状态
    private byte model3Status;
    //模块3温度
    private short model3Temperature;
    //模块4状态
    private byte model4Status;
    //模块4温度
    private short model4Temperature;
    //模块5状态
    private byte model5Status;
    //模块5温度
    private short model5Temperature;
    //模块6状态
    private byte model6Status;
    //模块6温度
    private short model6Temperature;
    //模块7状态
    private byte model7Status;
    //模块7温度
    private short model7Temperature;
    //模块8状态
    private byte model8Status;
    //模块8温度
    private short model8Temperature;
    //模块9状态
    private byte model9Status;
    //模块9温度
    private short model9Temperature;
    //模块10状态
    private byte model10Status;
    //模块10温度
    private short model10Temperature;
}
