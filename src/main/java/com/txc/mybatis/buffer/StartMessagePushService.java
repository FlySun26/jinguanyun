package com.txc.mybatis.buffer;

import com.txc.mybatis.bean.StartMessageCodeMessage;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyEncoderInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @ClassName StartMessagePushService
 * @Description TODO
 * @Date 2023/8/28 11:47
 * @Vertion 1.0
 **/
@Service
public class StartMessagePushService implements MyEncoderInterface {

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Message msg, ByteBuf out) {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        //报文标识
        out.writeShortLE(501);
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
        //消息体长度
        out.writeShortLE(22);
        StartMessageCodeMessage codeMessage=new StartMessageCodeMessage();
        try {
            codeMessage = (StartMessageCodeMessage) msg.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeByte(codeMessage.getSpearNum());
        byte[] bytes = codeMessage.getTransactionSerialNum().getBytes(StandardCharsets.UTF_8);
        out.writeBytes(bytes);
        out.writeIntLE(codeMessage.getCardBalance());
        out.writeByte(codeMessage.getStartType());
        out.writeByte(codeMessage.getBMSVoltage());
        out.writeByte(codeMessage.getSOCValue());
        out.writeShortLE(codeMessage.getAmountValue());
        out.writeBytes(bytes);
        if (bytes.length < 8) {
            for (int i = 0; i < 8 - bytes.length; i++) {
                out.writeByte(0);
            }
        }
    }
}
