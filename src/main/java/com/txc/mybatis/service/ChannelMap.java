package com.txc.mybatis.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ChannelMap
 * @Description TODO
 * @Date 2023/8/15 10:23
 * @Vertion 1.0
 **/
public class ChannelMap {
    /**
     * 管理一个全局map，保存连接进服务端的通道数量
     */
    private static final ConcurrentHashMap<String, Channel> CHANNEL_MAP = new ConcurrentHashMap<>(128);

    public static ConcurrentHashMap<String, Channel> getChannelMap() {
        return CHANNEL_MAP;
    }

    /**
     *  获取指定name的channel
     */
    public static Channel getChannelByName(String channelId){
        if(CollectionUtils.isEmpty(CHANNEL_MAP)){
            return null;
        }
        return CHANNEL_MAP.get(channelId);
    }

    /**
     *  将通道中的消息推送到每一个客户端
     */
    public static boolean pushNewsToAllClient(String obj){
        if(CollectionUtils.isEmpty(CHANNEL_MAP)){
            return false;
        }
        for(String channelId: CHANNEL_MAP.keySet()) {
            Channel channel = CHANNEL_MAP.get(channelId);
            channel.writeAndFlush(new TextWebSocketFrame(obj));
        }
        return true;
    }

    /**
     *  将channel和对应的name添加到ConcurrentHashMap
     */
    public static void addChannel(String channelId,Channel channel){
        CHANNEL_MAP.put(channelId,channel);
    }

    /**
     *  移除掉name对应的channel
     */
    public static boolean removeChannelByName(ChannelId channelId){
        if(CHANNEL_MAP.containsKey(channelId)){
            CHANNEL_MAP.remove(channelId);
            return true;
        }
        return false;
    }
}
