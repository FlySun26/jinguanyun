package com.txc.mybatis.service;

import com.txc.mybatis.bean.HostStatusMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @ClassName HostStatusMessageBuffer
 * @Description TODO
 * @Date 2023/8/24 10:15
 * @Vertion 1.0
 **/
public class HostStatusMessageBufferService implements MyInterface{
    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {

        //主机进风温度
        short hostInletTemperature = in.readShortLE();
        //主机出风温度
        short hostOutletTemperature = in.readShortLE();



        //模块1状态
        byte model1Status = in.readByte();
        //模块1温度
        short model1Temperature = in.readShortLE();
        //模块2状态
        byte model2Status = in.readByte();
        //模块2温度
        short model2Temperature = in.readShortLE();
        //模块3状态
        byte model3Status = in.readByte();
        //模块3温度
        short model3Temperature = in.readShortLE();
        //模块4状态
        byte model4Status = in.readByte();
        //模块4温度
        short model4Temperature = in.readShortLE();
        //模块5状态
        byte model5Status = in.readByte();
        //模块5温度
        short model5Temperature = in.readShortLE();
        //模块6状态
        byte model6Status = in.readByte();
        //模块6温度
        short model6Temperature = in.readShortLE();
        //模块7状态
        byte model7Status = in.readByte();
        //模块7温度
        short model7Temperature = in.readShortLE();
        //模块8状态
        byte model8Status = in.readByte();
        //模块8温度
        short model8Temperature = in.readShortLE();
        //模块9状态
        byte model9Status = in.readByte();
        //模块9温度
        short model9Temperature = in.readShortLE();
        //模块10状态
        byte model10Status = in.readByte();
        //模块10温度
        short model10Temperature = in.readShortLE();

        HostStatusMessage hostStatusMessage = new HostStatusMessage();
        hostStatusMessage.setModel1Status(model1Status);
        hostStatusMessage.setModel1Temperature(model1Temperature);
        return null;
    }


}
