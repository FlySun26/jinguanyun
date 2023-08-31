package com.txc.mybatis.buffer;

import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.util.CRCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName StartMessageResultService
 * @Description TODO
 * @Date 2023/8/29 08:36
 * @Vertion 1.0
 **/
@Service
@Slf4j
public class StartMessageResultService implements MyInterface {
    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {
        byte spearNum = in.readByte();
        short result = in.readShortLE();
        String transactionSerialNum = CRCUtil.convertByteBufToString(in.readBytes(8));
        log.info("收到开机确认,桩号：{},枪编码：{},结果：{},交易流水号：{}", message.getDevAddr(), spearNum, result, transactionSerialNum);
        return message;
    }
}
