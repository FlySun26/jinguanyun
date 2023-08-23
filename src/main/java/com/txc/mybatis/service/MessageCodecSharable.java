package com.txc.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txc.mybatis.bean.RegisterMessage;
import com.txc.mybatis.util.CRCUtil;
import com.txc.mybatis.util.MyInterfaceBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Slf4j
@ChannelHandler.Sharable
/**
 * 必须和 LengthFieldBasedFrameDecoder 一起使用，确保接到的 ByteBuf 消息是完整的
 */
@Component
public class MessageCodecSharable extends MessageToMessageCodec<ByteBuf, Message> {



    @Override
    public void encode(ChannelHandlerContext ctx, Message msg, List<Object> outList) throws Exception {
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
        ByteBuf out = ctx.alloc().buffer();
        //标识符
        out.writeBytes(new byte[]{(byte) 0XA5, (byte) 0XA5});
        //通讯协议版本
        out.writeShortLE(msg.getCommVersion());
        //加密方式
        out.writeByte(msg.getEncryModel());

        MyInterface myInterface = MyInterfaceBuilder.getMyInterface(String.valueOf(msg.getMessageType()));
        myInterface.encode(out,msg,outList);
        String crc = null;
        try {
            crc = CRCUtil.getCRC(CRCUtil.convertByteBufToString(out));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //校验位
        int i = Integer.parseInt(crc, 16);
        out.writeShort(i);
        outList.add(out);

    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
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
        if (!CRCUtil.crcCheck(in)) {
            return;
        }

        int identify = in.readShort();
        short commVersion = in.readShortLE();
        byte encryModel = in.readByte();
        short messageType = in.readShortLE();
        log.info("加载客户端报文,消息类型:{}", messageType);
        byte vendorld = in.readByte();
        int devAddr = in.readIntLE();
        int i = in.readMedium();
        int i1 = in.readMedium();
        short dataLength = in.readShortLE();

//        //把帧头整体读一遍
//        in.readerIndex(0);
//        byte[] bytes0 = new byte[20];
//        in.readBytes(bytes0, 0, 20);
//        log.info("加载客户端报文,消息帧头:{}", bytes0);
//        //读消息域
//        byte[] bytes = new byte[dataLength];
//        ByteBuf codeBuf = in.readBytes(bytes, 0, dataLength);
//
//        log.info("加载客户端报文,消息内容:{}", bytes);
//        //读校验位
//        short checksum = in.readShortLE();
//
//        //把帧头和消息域拼一块
//        byte[] allBytes = Arrays.copyOf(bytes0, bytes0.length + bytes.length);
//        System.arraycopy(bytes, 0, allBytes, bytes0.length, bytes.length);
//
//        //整体报文(除校验位)
//        String allHexString = ConvertCode.bytesToHexString(allBytes);
//        //帧头
//        String hexString0 = ConvertCode.bytesToHexString(bytes0);
//        //消息域
//        String hexString = ConvertCode.bytesToHexString(bytes);

        Message message = null;
        //判断到底给什么子类
        message = new RegisterRequestMessage();
        message.setIdentify(identify);
        message.setCommVersion(commVersion);
        message.setEncryModel(encryModel);
        message.setMessageType(messageType);
        message.setVendorld(vendorld);
        message.setDevAddr(devAddr);
        message.setMessageTime("230708233800");
        message.setDataLength(dataLength);
        MyInterface myInterface = MyInterfaceBuilder.getMyInterface(String.valueOf(messageType));
        myInterface.decode(ctx, message, in, out);
        out.add(message);
    }


}
