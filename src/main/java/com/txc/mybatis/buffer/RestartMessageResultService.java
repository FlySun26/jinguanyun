package com.txc.mybatis.buffer;

import com.txc.mybatis.service.Message;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.util.CRCUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @ClassName RestartMessageResultService
 * @Description TODO
 * @Date 2023/9/1 15:12
 * @Vertion 1.0
 **/
@Data
@Slf4j
public class RestartMessageResultService implements MyInterface {
    @Override
    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    @Override
    public Message decode(ChannelHandlerContext ctx, Message message, ByteBuf in, List<Object> out) throws Exception {
        short result = in.readShortLE();
        log.info("收到重启确认,桩号：{},枪编码：{},结果：{}", message.getDevAddr(), result);
        return message;
    }
}
