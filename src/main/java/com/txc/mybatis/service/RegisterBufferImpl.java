package com.txc.mybatis.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

/**
 * @ClassName RegisterBufferImpl
 * @Description TODO
 * @Date 2023/8/22 09:04
 * @Vertion 1.0
 **/
public class RegisterBufferImpl implements MyInterface {

    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    @Override
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {

    }
}
