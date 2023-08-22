package com.txc.mybatis.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

public interface MyInterface {


    public void encode(ByteBuf out, Message msg, List<Object> outList);

    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out);
}
