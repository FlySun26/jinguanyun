package com.txc.mybatis.service;

import com.txc.mybatis.util.CRCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName ChargeMessageService
 * @Description TODO
 * @Date 2023/8/23 11:14
 * @Vertion 1.0
 **/
@Service
public class ChargeMessageService implements MyInterface{
    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {
        byte num = in.readByte();
        String s = CRCUtil.convertByteBufToString(in.readBytes(8));
        System.out.println(s);
        return null;
    }
}
