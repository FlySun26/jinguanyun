package com.txc.mybatis.service;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @ClassName MyEncoder
 * @Description TODO
 * @Date 2023/8/15 10:16
 * @Vertion 1.0
 **/
public class MyEncoder extends MessageToByteEncoder<String> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, String s, ByteBuf byteBuf) throws Exception {

    }
}
