package com.txc.mybatis.service;

import com.alibaba.fastjson.JSON;
import com.txc.mybatis.bean.Heartbeat;
import com.txc.mybatis.bean.SpearParam;
import com.txc.mybatis.util.CRCUtil;
import com.txc.mybatis.util.JedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName HeartbeatBufferImpl
 * @Description TODO
 * @Date 2023/8/22 10:21
 * @Vertion 1.0
 **/
@Service
public class HeartbeatBufferImpl implements MyInterface{

    @Resource
    private RedisTemplate redisTemplate;

    public static RedisTemplate redisTemplateStatic;

    @Resource
    private ErrorSpearService errorSpearService;

    public static ErrorSpearService errorSpearServiceStatic;

    @PostConstruct
    public void init() {
        errorSpearServiceStatic = errorSpearService;
        redisTemplateStatic = redisTemplate;
    }
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
        out.writeShortLE(308);
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

        out.writeShortLE(8);
        out.writeShortLE(0);
        //时间戳（BCD码）


        out.writeByte(Integer.parseInt(year + "", 16));
        out.writeByte(Integer.parseInt(month + "", 16));
        out.writeByte(Integer.parseInt(day + "", 16));
        out.writeByte(Integer.parseInt(hour + "", 16));
        out.writeByte(Integer.parseInt(minute + "", 16));
        out.writeByte(Integer.parseInt(second + "", 16));



    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) {
        Heartbeat heartbeat = new Heartbeat();
        heartbeat.setDevAddr(message.getDevAddr());
        byte b = in.readByte();
        heartbeat.setSpearNum((int) b);
        List<SpearParam> list = new ArrayList<>();
        for (byte i2 = 1; i2 <= b; i2++) {
            byte status = in.readByte();
            short i3 = in.readShortLE();
            short i4 = in.readShortLE();
            short i5 = in.readShortLE();
            SpearParam spearParam = new SpearParam();
            spearParam.setChargeStatus((int) status);
            spearParam.setParam1((int) i3);
            spearParam.setParam2((int) i4);
            spearParam.setParam3((int) i5);
            list.add(spearParam);
            //枪故障
            if (status == 7) {
                errorSpearServiceStatic.saveErrorCode(message.getDevAddr(), spearParam, i2);
            }
        }
        heartbeat.setSpearParamList(list);

        redisTemplateStatic.opsForValue()
                .set("heartbuff:spearstatus:" + message.getDevAddr(), JSON.toJSONString(heartbeat));
        //JedisUtil.set("heartbuff:spearstatus:" + message.getDevAddr(), JSON.toJSONString(heartbeat));
        return message;
    }

}
