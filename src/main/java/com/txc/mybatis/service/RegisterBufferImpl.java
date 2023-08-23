package com.txc.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txc.mybatis.bean.ChargingStation;
import com.txc.mybatis.bean.RegisterMessage;
import com.txc.mybatis.util.CRCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName RegisterBufferImpl
 * @Description TODO
 * @Date 2023/8/22 09:04
 * @Vertion 1.0
 **/
@Slf4j
@Service
public class RegisterBufferImpl implements MyInterface {

    @Resource
    private RegisterMessageService registerMessageService;

    @Resource
    private ChargingStationService chargingStationService;

    public static ChargingStationService chargingStationServiceStatic;


    public static RegisterMessageService registerMessageServiceStatic;

    @PostConstruct
    public void init() {
        registerMessageServiceStatic = registerMessageService;
        chargingStationServiceStatic = chargingStationService;
    }


    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

        //报文标识
        out.writeShortLE(102);
        //厂商标识
        out.writeByte(msg.getVendorld());
        //充电设备编号
        out.writeIntLE(msg.getDevAddr());
        //时间戳（BCD码）
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        out.writeByte(Integer.parseInt(year + "", 16));
        out.writeByte(Integer.parseInt(month + "", 16));
        out.writeByte(Integer.parseInt(day + "", 16));
        out.writeByte(Integer.parseInt(hour + "", 16));
        out.writeByte(Integer.parseInt(minute + "", 16));
        out.writeByte(Integer.parseInt(second + "", 16));
        //消息体长度
        out.writeShortLE(56);

        out.writeShortLE(0);
        out.writeByte(Integer.parseInt(year + "", 16));
        out.writeByte(Integer.parseInt(month + "", 16));
        out.writeByte(Integer.parseInt(day + "", 16));
        out.writeByte(Integer.parseInt(hour + "", 16));
        out.writeByte(Integer.parseInt(minute + "", 16));
        out.writeByte(Integer.parseInt(second + "", 16));
        byte[] bytes = "http://wx.nyjinguan.com/app/download?code=".getBytes(StandardCharsets.UTF_8);
        out.writeBytes(bytes);
        out.writeShortLE(0);
        out.writeIntLE(0);
    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {
        long count = chargingStationServiceStatic.count(new QueryWrapper<ChargingStation>().lambda().eq(ChargingStation::getDevAddr, message.getDevAddr()));
        if (count > 0) {
            List<RegisterMessage> list = registerMessageServiceStatic.list(new QueryWrapper<RegisterMessage>().lambda().eq(RegisterMessage::getDevAddr, message.getDevAddr()));
            if (CollectionUtils.isEmpty(list)) {
                RegisterMessage registerMessage = new RegisterMessage();
                registerMessage.setStatus(1);
                registerMessage.setChannelId(ctx.channel().id().toString());
                registerMessage.setDevAddr(message.getDevAddr());
                registerMessageServiceStatic.save(registerMessage);

            } else {
                RegisterMessage registerMessage = list.stream().findFirst().orElse(null);
                registerMessage.setChannelId(ctx.channel().id().toString());
                registerMessage.setStatus(1);
                registerMessageServiceStatic.updateById(registerMessage);
            }
        }else {
            ctx.close();
        }
        return message;
    }
}
