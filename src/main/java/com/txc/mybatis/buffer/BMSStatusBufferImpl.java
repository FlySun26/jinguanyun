package com.txc.mybatis.buffer;

import com.alibaba.fastjson.JSON;
import com.txc.mybatis.bean.BMSStatus;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.util.CRCUtil;
import com.txc.mybatis.util.JedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName BMSStatusImpl
 * @Description TODO
 * @Date 2023/8/25 13:41
 * @Vertion 1.0
 **/
@Service
public class BMSStatusBufferImpl implements MyInterface {
    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        //报文标识
        out.writeShortLE(302);
        //厂商标识
        out.writeByte(msg.getVendorld());
        //充电设备编号
        out.writeIntLE(msg.getDevAddr());

        out.writeByte(Integer.parseInt(year + "", 16));
        out.writeByte(Integer.parseInt(month + "", 16));
        out.writeByte(Integer.parseInt(day + "", 16));
        out.writeByte(Integer.parseInt(hour + "", 16));
        out.writeByte(Integer.parseInt(minute + "", 16));
        out.writeByte(Integer.parseInt(second + "", 16));
        //报文长度
        out.writeShortLE(9);
        BMSStatus bmsStatus = (BMSStatus) msg.getObject();
        out.writeByte(bmsStatus.getSpearNum());
        out.writeBytes(bmsStatus.getTransactionSerialNum().getBytes(StandardCharsets.UTF_8));

    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {

        //枪编号
        byte spearNum = in.readByte();
        //交易流水号
        String transactionSerialNum = CRCUtil.convertByteBufToString(in.readBytes(8));
        //卡号
        int cardNum = in.readIntLE();
        //卡内余额
        int cardBalance = in.readIntLE();
        //启动方式
        byte startType = in.readByte();
        //BMS版本
        byte BMSType = in.readByte();
        //电池类型
        byte batteryType = in.readByte();
        //额定容量
        short ratedCapacity = in.readShortLE();
        //额定总电压
        short RatedTotalVoltage = in.readShortLE();
        //BMS生产厂商
        int BMSManufacturer = in.readIntLE();
        //电池组序号
        int batteryGroupNum = in.readIntLE();
        //电池组生产日期年
        byte batteryGroupManufactureDateYear = in.readByte();
        //电池组生产日期月
        byte batteryGroupManufactureDateMonth = in.readByte();
        //电池组生产日期日
        byte batteryGroupManufactureDateDay = in.readByte();
        //电池组充电次数
        int batteryGroupChargingCycles = in.readIntLE();
        //电池电压
        short batteryVoltage = in.readShortLE();
        //允许温度
        short permissiveTemperature = in.readShortLE();
        //VIN
        String VIN = CRCUtil.convertByteBufToString(in.readBytes(17));
        //开始SOC
        byte startSOC = in.readByte();
        //绝缘检测电压
        short insulationDetectionVoltage = in.readShortLE();
        //正级阻值
        short positiveResistanceValue = in.readShortLE();
        //负极阻值
        short negativeResistanceValue = in.readShortLE();
        //开始时间
        String startTime = CRCUtil.convertByteBufToString(in.readBytes(6));
        //电表度数
        int electricityMeter = in.readIntLE();
        BMSStatus bmsStatus = new BMSStatus();
        bmsStatus.setSpearNum((int) spearNum);
        bmsStatus.setCardNum(cardNum);
        bmsStatus.setTransactionSerialNum(transactionSerialNum);
        bmsStatus.setCardBalance(cardBalance);
        bmsStatus.setStartType((int) startType);
        bmsStatus.setBMSType((int) BMSType);
        bmsStatus.setBatteryType(batteryGroupNum);
        bmsStatus.setBatteryType((int) batteryType);
        bmsStatus.setRatedCapacity((int) ratedCapacity);
        bmsStatus.setRatedTotalVoltage((int) RatedTotalVoltage);
        bmsStatus.setBMSManufacturer(BMSManufacturer);
        bmsStatus.setBatteryGroupManufactureDateYear((int) batteryGroupManufactureDateYear);
        bmsStatus.setBatteryGroupManufactureDateMonth((int) batteryGroupManufactureDateMonth);
        bmsStatus.setBatteryGroupManufactureDateDay((int) batteryGroupManufactureDateDay);
        bmsStatus.setBatteryGroupChargingCycles(batteryGroupChargingCycles);
        bmsStatus.setBatteryVoltage((int) batteryVoltage);
        bmsStatus.setPermissiveTemperature((int) permissiveTemperature);
        bmsStatus.setStartSOC((int) startSOC);
        bmsStatus.setVIN(VIN);
        bmsStatus.setInsulationDetectionVoltage((int) insulationDetectionVoltage);
        bmsStatus.setPositiveResistanceValue((int) positiveResistanceValue);
        bmsStatus.setNegativeResistanceValue((int) negativeResistanceValue);
        bmsStatus.setStartTime(startTime);
        bmsStatus.setElectricityMeter(electricityMeter);
        JedisUtil.set("bmsstatus:" + message.getDevAddr(), JSON.toJSONString(bmsStatus));
        message.setObject(bmsStatus);
        return message;
    }
}
