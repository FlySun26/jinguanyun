package com.txc.mybatis.util;

import com.txc.mybatis.service.ChargeMessageService;
import com.txc.mybatis.service.HeartbeatBufferImpl;
import com.txc.mybatis.service.HostStatusMessageBufferService;
import com.txc.mybatis.service.MyInterface;
import com.txc.mybatis.service.RegisterBufferImpl;

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
