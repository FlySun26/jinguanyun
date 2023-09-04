package com.txc.mybatis.util;

import com.txc.mybatis.buffer.BMSStatusBufferImpl;
import com.txc.mybatis.buffer.ChargeMessageService;
import com.txc.mybatis.buffer.FailStartMessageService;
import com.txc.mybatis.buffer.HeartbeatBufferImpl;
import com.txc.mybatis.buffer.HostStatusMessageBufferService;
import com.txc.mybatis.buffer.RegisterBufferImpl;
import com.txc.mybatis.buffer.StartMessagePushService;
import com.txc.mybatis.buffer.StartService;
import com.txc.mybatis.service.MyEncoderInterface;
import com.txc.mybatis.service.MyInterface;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName MyEncoderBuilder
 * @Description TODO
 * @Date 2023/8/28 10:00
 * @Vertion 1.0
 **/
public class MyEncoderBuilder {


    /**
     * MyEncoderBuilder缓存池
     */
    private static Map<String, MyEncoderInterface> myEncoderInterfacePool = new ConcurrentHashMap<>();
    //先创建所有实现类的实例放进map中
    static {
//        myEncoderInterfacePool.put("501", new StartMessagePushService());
    }

    /**
     * 获取MyInterface
     *
     * @param messageType 消息类型
     * @return MyInterface
     */
    public static MyEncoderInterface getMyInterface(String messageType) throws Exception {
        //根据前端传的类型返回不同的实例
        MyEncoderInterface myInterface = myEncoderInterfacePool.get(messageType);
        if (myInterface == null) {
            throw new Exception("no grantType was found");
        } else {
            return myInterface;
        }
    }
}
