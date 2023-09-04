package com.txc.mybatis.buffer;

import com.txc.mybatis.bean.RestartMessageCode;
import com.txc.mybatis.bean.SettingCurrentMessageCode;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName RestartMessagePushService
 * @Description TODO
 * @Date 2023/9/1 11:01
 * @Vertion 1.0
 **/
@Service
public class RestartMessagePushService implements MyInterface {
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
        out.writeShortLE(507);
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
        RestartMessageCode codeMessage = new RestartMessageCode();
        try {
            codeMessage = (RestartMessageCode) msg.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.writeByte(codeMessage.getRestartType());
        out.writeShortLE(codeMessage.getPasswordParam());
    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {
        return null;
    }
}
