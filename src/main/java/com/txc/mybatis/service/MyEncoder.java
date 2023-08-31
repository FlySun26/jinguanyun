package com.txc.mybatis.service;

import com.txc.mybatis.util.CRCUtil;
import com.txc.mybatis.util.MyEncoderBuilder;
import com.txc.mybatis.util.MyInterfaceBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * @ClassName MyEncoder
 * @Description TODO
 * @Date 2023/8/15 10:16
 * @Vertion 1.0
 **/
@Component
public class MyEncoder extends MessageToByteEncoder<Message> {

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Message message, ByteBuf byteBuf) throws Exception {

        /*
        域	        描述	       字段长度
        Identify	标识符	      2
        CommVersion	通讯协议版本	  2
        EncryModel	加密方式	      1
        MessageType	报文标识	      2
        Vendorld	厂商标识	      1
        DevAddr	    充电设备编号	  4
        MessageTime	时间戳（BCD码） 6
        DataLength	消息体长度	  2
        Payload	    消息体	      N
        Checksum	CRC校验位	  2
        */
        //标识符
        byteBuf.writeBytes(new byte[]{(byte) 0XA5, (byte) 0XA5});
        //通讯协议版本
        byteBuf.writeShortLE(message.getCommVersion());
        //加密方式
        byteBuf.writeByte(message.getEncryModel());

        MyEncoderInterface myInterface = MyEncoderBuilder.getMyInterface(String.valueOf(message.getMessageType()));
        myInterface.encode(channelHandlerContext, message, byteBuf);

        String crc = null;
        try {
            crc = CRCUtil.getCRC(CRCUtil.convertByteBufToString(byteBuf));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //校验位
        int i = Integer.parseInt(crc, 16);
        byteBuf.writeShort(i);
    }
}
