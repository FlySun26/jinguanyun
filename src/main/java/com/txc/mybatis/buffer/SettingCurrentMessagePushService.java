package com.txc.mybatis.buffer;

import com.txc.mybatis.bean.SettingCurrentMessageCode;
import com.txc.mybatis.bean.StartMessageCodeMessage;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyEncoderInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @ClassName SettingCurrentMessagePushService
 * @Description TODO
 * @Date 2023/8/31 14:06
 * @Vertion 1.0
 **/
@Service
public class SettingCurrentMessagePushService implements MyEncoderInterface {
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
        out.writeShortLE(505);
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
        out.writeShortLE(3);
        SettingCurrentMessageCode codeMessage = new SettingCurrentMessageCode();
        try {
            codeMessage = (SettingCurrentMessageCode) msg.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeByte(codeMessage.getSpearNum());
        out.writeShortLE(codeMessage.getCurrentParam());
    }
}
