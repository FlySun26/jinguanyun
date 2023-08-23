package com.txc.mybatis.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName NettyServerChannelInitializer
 * @Description TODO
 * @Date 2023/8/15 10:13
 * @Vertion 1.0
 **/
@Slf4j
public class NettyServerChannelInitializer extends ChannelInitializer<SocketChannel> {


//    private RegisterMessageService registerMessageService;
//
//    public NettyServerChannelInitializer(RegisterMessageService registerMessageService) {
//        this.registerMessageService = registerMessageService;
//    }

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        //接收消息格式,使用自定义解析数据格式
        //pipeline.addLast("decoder",new MyDecoder());
        pipeline.addLast(new ProcotolFrameDecoder());
        pipeline.addLast(new MessageCodecSharable());

        pipeline.addLast(new IdleStateHandler(30, 0, 0));
        //发送消息格式，使用自定义解析数据格式
        pipeline.addLast("encoder",new MyEncoder());

        //针对客户端，如果在1分钟时没有想服务端发送写心跳(ALL)，则主动断开
        //如果是读空闲或者写空闲，不处理,这里根据自己业务考虑使用
        //pipeline.addLast(new IdleStateHandler(2,0,0, TimeUnit.SECONDS));
        //自定义的空闲检测
        pipeline.addLast(new NettyServerHandler());

    }
}
