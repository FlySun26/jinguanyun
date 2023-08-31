package com.txc.mybatis.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface MyEncoderInterface {

    public void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf);
}
