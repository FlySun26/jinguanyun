package com.txc.mybatis.buffer;

import com.txc.mybatis.bean.FailStartMessage;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.util.CRCUtil;
import com.txc.mybatis.util.JedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName FailStartMessageService
 * @Description TODO
 * @Date 2023/8/25 16:53
 * @Vertion 1.0
 **/
@Data
public class FailStartMessageService implements MyInterface {
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
        out.writeShortLE(310);
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
        FailStartMessage failStartMessage = (FailStartMessage) msg.getObject();
        out.writeByte(failStartMessage.getSpearNum());
        out.writeBytes(failStartMessage.getTransactionSerialNum().getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {

        //充电枪编号
        byte spearNum = in.readByte();
        //交易流水号
        String transactionSerialNum = CRCUtil.convertByteBufToString(in.readBytes(8));
        //故障码
        short errorCode = in.readShortLE();
        //参数1
        short param1 = in.readShortLE();
        //参数2
        short param2 = in.readShortLE();
        FailStartMessage failStartMessage = new FailStartMessage();
        failStartMessage.setSpearNum((int) spearNum);
        failStartMessage.setTransactionSerialNum(transactionSerialNum);
        failStartMessage.setErrorCode((int) errorCode);
        failStartMessage.setParam1((int) param1);
        failStartMessage.setParam2((int) param2);
        message.setObject(failStartMessage);
        JedisUtil.set("failstartmessage:" + message.getDevAddr(), failStartMessage);
        return message;
    }
}
