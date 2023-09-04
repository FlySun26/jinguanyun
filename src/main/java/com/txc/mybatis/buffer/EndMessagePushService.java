package com.txc.mybatis.buffer;

import com.txc.mybatis.bean.StartMessageCodeMessage;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyEncoderInterface;
import com.txc.mybatis.service.MyInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName EndMessagePushService
 * @Description TODO
 * @Date 2023/8/31 11:01
 * @Vertion 1.0
 **/
@Service
public class EndMessagePushService implements MyInterface {

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
        out.writeShortLE(503);
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
        out.writeShortLE(9);
        StartMessageCodeMessage codeMessage=new StartMessageCodeMessage();
        try {
            codeMessage = (StartMessageCodeMessage) msg.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeByte(codeMessage.getSpearNum());
        byte[] bytes = codeMessage.getTransactionSerialNum().getBytes(StandardCharsets.UTF_8);
        out.writeBytes(bytes);
        if (bytes.length < 8) {
            for (int i = 0; i < 8 - bytes.length; i++) {
                out.writeByte(0);
            }
        }
    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {
        return null;
    }
}
