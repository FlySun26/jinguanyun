package com.txc.mybatis.buffer;

import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyEncoderInterface;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

/**
 * @ClassName StartService
 * @Description TODO
 * @Date 2023/8/28 10:03
 * @Vertion 1.0
 **/
public class StartService implements MyEncoderInterface {

    @Override
    public void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) {

    }
}
