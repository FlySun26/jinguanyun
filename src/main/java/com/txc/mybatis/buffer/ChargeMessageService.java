package com.txc.mybatis.buffer;

import cn.hutool.extra.template.Template;
import com.alibaba.fastjson.JSON;
import com.txc.mybatis.bean.ChargeMessage;
import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.util.CRCUtil;
import com.txc.mybatis.util.JedisUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ChargeMessageService
 * @Description TODO
 * @Date 2023/8/23 11:14
 * @Vertion 1.0
 **/
@Service
public class ChargeMessageService implements MyInterface {

    @Resource
    private RedisTemplate redisTemplate;

    public static RedisTemplate redisTemplateStatic;

    @PostConstruct
    public void init() {

        redisTemplateStatic = redisTemplate;
    }
    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {

        //充电枪编号
        byte num = in.readByte();
        //交易流水号
        String serialNumber = CRCUtil.convertByteBufToString(in.readBytes(8));
        //卡号
        int cardNum = in.readIntLE();
        //启动方式
        byte startType = in.readByte();
        //电压
        short voltage = in.readShortLE();
        //电流
        short current = in.readShortLE();
        //正极温度
        short positiveElectrodeTemperature = in.readShortLE();
        //负极温度
        short negativeElectrodeTemperature = in.readShortLE();
        //充电时长
        short chargeTime = in.readShortLE();
        //充电电量
        int chargeDosage = in.readIntLE();
        //充电费用
        int chargeCost = in.readIntLE();
        //SOC
        byte SOC = in.readByte();
        //需求电压
        short demandVoltage = in.readShortLE();
        //需求电流
        short demanCdurrent = in.readShortLE();
        //单体最高温度
        short maxTemperature = in.readShortLE();
        //单体最低温度
        short minTemperature = in.readShortLE();
        //单体最高电压
        short maxVoltage = in.readShortLE();

        ChargeMessage chargeMessage = new ChargeMessage();
        chargeMessage.setChargeCost(chargeCost);
        chargeMessage.setChargeTime((int) chargeTime);
        chargeMessage.setChargeDosage(chargeDosage);
        chargeMessage.setCurrent((int) current);
        chargeMessage.setCardNum(cardNum);
        chargeMessage.setDemanCdurrent((int) demanCdurrent);
        chargeMessage.setDemandVoltage((int) demandVoltage);
        chargeMessage.setNum((int) num);
        chargeMessage.setVoltage((int) voltage);
        chargeMessage.setPositiveElectrodeTemperature((int) positiveElectrodeTemperature);
        chargeMessage.setNegativeElectrodeTemperature((int) negativeElectrodeTemperature);
        chargeMessage.setMaxVoltage((int) maxVoltage);
        chargeMessage.setMaxTemperature((int) maxTemperature);
        chargeMessage.setMinTemperature((int) minTemperature);
        chargeMessage.setSOC((int) SOC);
        redisTemplateStatic.opsForValue().set("chargemessage:" + message.getDevAddr() + ":" + num, JSON.toJSONString(chargeMessage));
        //JedisUtil.set("chargemessage:" + message.getDevAddr() + ":" + num, JSON.toJSONString(chargeMessage));
        return message;
    }
}
