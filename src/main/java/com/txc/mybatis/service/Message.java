package com.txc.mybatis.service;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public abstract class Message implements Serializable {


    /**
     * 根据消息类型字节，获得对应的消息 class
     * @param messageType 消息类型字节
     * @return 消息 class
     */
//    public static Class<? extends Message> getMessageClass(int messageType) {
//        return messageClasses.get(messageType);
//    }
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

    private int identify;
    private int commVersion;
    private int encryModel;
    private int messageType;
    private int vendorld;
    private int devAddr;
    private String messageTime;
    private int dataLength;
    private String code;//16进制
    private int checksum;

    private Object object;

    private String channelId;


//    public abstract int getMessageType();
//
//    public static final int LoginRequestMessage = 0;
//    public static final int LoginResponseMessage = 1;
//
//    public static final int RegisterRequestMessage = 101;
//
//
//    private static final Map<Integer, Class<? extends Message>> messageClasses = new HashMap<>();
//
//    static {
//        messageClasses.put(LoginRequestMessage, LoginRequestMessage.class);
//        messageClasses.put(LoginResponseMessage, LoginResponseMessage.class);
//        messageClasses.put(RegisterRequestMessage, RegisterRequestMessage.class);
//    }

}
