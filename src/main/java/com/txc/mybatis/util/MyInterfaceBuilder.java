package com.txc.mybatis.util;

import com.txc.mybatis.buffer.BMSStatusBufferImpl;
import com.txc.mybatis.buffer.ChargeMessageService;
import com.txc.mybatis.buffer.EndMessagePushService;
import com.txc.mybatis.buffer.EndMessageResultService;
import com.txc.mybatis.buffer.FailStartMessageService;
import com.txc.mybatis.buffer.HeartbeatBufferImpl;
import com.txc.mybatis.buffer.HostStatusMessageBufferService;
import com.txc.mybatis.buffer.SettingCurrentMessagePushService;
import com.txc.mybatis.buffer.SettingCurrentMessageResultService;
import com.txc.mybatis.buffer.StartMessagePushService;
import com.txc.mybatis.buffer.StartMessageResultService;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.buffer.RegisterBufferImpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MyInterfaceBuilder {

    /**
     * MyInterfaceBuilder缓存池
     */
    private static Map<String, MyInterface> myInterfacePool = new ConcurrentHashMap<>();
    //先创建所有实现类的实例放进map中
    static {
        myInterfacePool.put("101", new RegisterBufferImpl());
        myInterfacePool.put("307", new HeartbeatBufferImpl());
        myInterfacePool.put("304", new ChargeMessageService());
        myInterfacePool.put("305", new HostStatusMessageBufferService());
        myInterfacePool.put("301", new BMSStatusBufferImpl());
        myInterfacePool.put("309", new FailStartMessageService());
        myInterfacePool.put("501", new StartMessagePushService());
        myInterfacePool.put("502", new StartMessageResultService());
        myInterfacePool.put("503", new EndMessagePushService());
        myInterfacePool.put("504", new EndMessageResultService());
        myInterfacePool.put("505", new SettingCurrentMessagePushService());
        myInterfacePool.put("506", new SettingCurrentMessageResultService());
    }

    /**
     * 获取MyInterface
     *
     * @param messageType 消息类型
     * @return MyInterface
     */
    public static MyInterface getMyInterface(String messageType) throws Exception {
        //根据前端传的类型返回不同的实例
        MyInterface myInterface = myInterfacePool.get(messageType);
        if (myInterface == null) {
            throw new Exception("no grantType was found");
        } else {
            return myInterface;
        }
    }

}
