package com.txc.mybatis.service;

import cn.hutool.core.util.HexUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txc.mybatis.bean.RegisterMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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



    private RegisterMessageService registerMessageService;

    public MessageCodecSharable(RegisterMessageService registerMessageService) {
        this.registerMessageService = registerMessageService;
    }


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
        //报文标识
        out.writeShortLE(102);
        //厂商标识
        out.writeByte(msg.getVendorld());
        //充电设备编号
        out.writeIntLE(msg.getDevAddr());
        //时间戳（BCD码）
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        out.writeByte(Integer.parseInt(year + "", 16));
        out.writeByte(Integer.parseInt(month + "", 16));
        out.writeByte(Integer.parseInt(day + "", 16));
        out.writeByte(Integer.parseInt(hour + "", 16));
        out.writeByte(Integer.parseInt(minute + "", 16));
        out.writeByte(Integer.parseInt(second + "", 16));

        //消息体长度
        out.writeShortLE(56);

        out.writeShortLE(0);

        out.writeByte(Integer.parseInt(year + "", 16));
        out.writeByte(Integer.parseInt(month + "", 16));
        out.writeByte(Integer.parseInt(day + "", 16));
        out.writeByte(Integer.parseInt(hour + "", 16));
        out.writeByte(Integer.parseInt(minute + "", 16));
        out.writeByte(Integer.parseInt(second + "", 16));
        byte[] bytes = "http://wx.nyjinguan.com/app/download?code=".getBytes(StandardCharsets.UTF_8);
        out.writeBytes(bytes);
        out.writeShortLE(0);
        out.writeIntLE(0);

//        //获取内容的字节数组
//        String code = msg.getCode();
//        byte[] bytes = ConvertCode.hexString2Bytes(code);
        //byte[] bytes = Config.getSerializerAlgorithm().serialize(msg);
        //写入内容
//        out.writeBytes(bytes);
        String crc = CRCUtil.getCRC(CRCUtil.convertByteBufToString(out));
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

        //把帧头整体读一遍
        in.readerIndex(0);
        byte[] bytes0 = new byte[20];
        in.readBytes(bytes0, 0, 20);
        log.info("加载客户端报文,消息帧头:{}", bytes0);
        //读消息域
        byte[] bytes = new byte[dataLength];
        in.readBytes(bytes, 0, dataLength);
        log.info("加载客户端报文,消息内容:{}", bytes);
        //读校验位
        short checksum = in.readShortLE();

        //把帧头和消息域拼一块
        byte[] allBytes = Arrays.copyOf(bytes0, bytes0.length + bytes.length);
        System.arraycopy(bytes, 0, allBytes, bytes0.length, bytes.length);

        //整体报文(除校验位)
        String allHexString = ConvertCode.bytesToHexString(allBytes);
        //帧头
        String hexString0 = ConvertCode.bytesToHexString(bytes0);
        //消息域
        String hexString = ConvertCode.bytesToHexString(bytes);

        Message message = null;
        //判断到底给什么子类
        message = new RegisterRequestMessage(hexString);
        message.setIdentify(identify);
        message.setCommVersion(commVersion);
        message.setEncryModel(encryModel);
        message.setMessageType(messageType);
        message.setVendorld(vendorld);
        message.setDevAddr(devAddr);
        message.setMessageTime("230708233800");
        message.setDataLength(dataLength);
        message.setCode(hexString);
        message.setChecksum(checksum);
        //out.add(identify+","+commVersion+","+encryModel+","+messageType+","+vendorld+","+devAddr+","+"230708233800,"+dataLength+","+hexString);
        out.add(message);

        List<RegisterMessage> list = registerMessageService.list(new QueryWrapper<RegisterMessage>().lambda().eq(RegisterMessage::getDevAddr, message.getDevAddr()));
        if (CollectionUtils.isEmpty(list)) {
            RegisterMessage registerMessage = new RegisterMessage();
            registerMessage.setStatus(1);
            registerMessage.setChannelId(ctx.channel().id().toString());
            registerMessage.setDevAddr(message.getDevAddr());
            registerMessageService.save(registerMessage);


        } else {
            RegisterMessage registerMessage = list.stream().findFirst().orElse(null);
            registerMessage.setChannelId(ctx.channel().id().toString());
            registerMessage.setStatus(1);
            registerMessageService.updateById(registerMessage);
        }

    }


}
