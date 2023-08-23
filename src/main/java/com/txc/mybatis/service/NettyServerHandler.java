package com.txc.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txc.mybatis.bean.RegisterMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelId;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.List;
import java.util.logging.SocketHandler;

/**
 * @ClassName NettyServerHandler
 * @Description TODO
 * @Date 2023/8/15 10:16
 * @Vertion 1.0
 **/
@Slf4j
@Component
public class NettyServerHandler extends ChannelDuplexHandler {





    @Resource
    private RegisterMessageService registerMessageService;

    private static RegisterMessageService registerMessageServiceStatic;

    @PostConstruct
    public void init() {
        registerMessageServiceStatic = registerMessageService;
    }

    /**
     * 功能描述: 有客户端连接服务器会触发此函数
     * @Author sxf
     * @Date 2022/8/26
     * @param  ctx 通道
     * @return void
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = insocket.getAddress().getHostAddress();
        int clientPort = insocket.getPort();
        //获取连接通道唯一标识
        ChannelId channelId = ctx.channel().id();
        //如果map中不包含此连接，就保存连接
        if (ChannelMap.getChannelMap().containsKey(channelId)) {
            log.info("客户端:{},是连接状态，连接通道数量:{} ",channelId,ChannelMap.getChannelMap().size());
        } else {
            //保存连接
            ChannelMap.addChannel(channelId, ctx.channel());
            log.info("客户端:{},连接netty服务器[IP:{}-->PORT:{}]",channelId, clientIp,clientPort);
            log.info("连接通道数量: {}",ChannelMap.getChannelMap().size());
        }
    }

    /**
     * 功能描述: 有客户端终止连接服务器会触发此函数
     * @Author sxf
     * @Date 2022/8/26
     * @param  ctx 通道处理程序上下文
     * @return void
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        InetSocketAddress inSocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String clientIp = inSocket.getAddress().getHostAddress();
        ChannelId channelId = ctx.channel().id();
        //包含此客户端才去删除
        if (ChannelMap.getChannelMap().containsKey(channelId)) {
            //删除连接
            ChannelMap.getChannelMap().remove(channelId);
            log.info("客户端:{},连接netty服务器[IP:{}-->PORT:{}]",channelId, clientIp,inSocket.getPort());
            log.info("连接通道数量: " + ChannelMap.getChannelMap().size());
        }
    }

//    /**
//     * 功能描述: 有客户端发消息会触发此函数
//     * @Author sxf
//     * @Date 2022/8/26
//     * @param  ctx 通道处理程序上下文
//     * @param  msg 客户端发送的消息
//     * @return void
//     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("加载客户端报文,客户端id:{},客户端消息111:{}",ctx.channel().id(), msg);
        //响应客户端
        this.channelWrite(ctx.channel().id(), msg);
    }

   /* @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        String bytes = "01 03 00 02 00 01 25 CA";
        ctx.writeAndFlush(bytes);
    }*/

    /**
     * 功能描述: 服务端给客户端发送消息
     * @Author sxf
     * @Date 2022/8/26
     * @param  channelId 连接通道唯一id
     * @param  msg 需要发送的消息内容
     * @return void
     */
    public void channelWrite(ChannelId channelId, Object msg) throws Exception {
        Channel channel = ChannelMap.getChannelMap().get(channelId);
        if (channel == null) {
            log.info("通道:{},不存在",channelId);
            return;
        }
        if (msg == null || msg == "") {
            log.info("服务端响应空的消息");
            return;
        }
        //将客户端的信息直接返回写入ctx
        channel.write(msg);
        //刷新缓存区
        channel.flush();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        String socketString = ctx.channel().remoteAddress().toString();
//        if (evt instanceof IdleStateEvent) {
//            IdleStateEvent event = (IdleStateEvent) evt;
//            if (event.state() == IdleState.READER_IDLE) {
//                log.info("Client:{},READER_IDLE 读超时",socketString);
//                ctx.disconnect();
//                Channel channel = ctx.channel();
//                ChannelId id = channel.id();
//                ChannelMap.removeChannelByName(id);
//            } else if (event.state() == IdleState.WRITER_IDLE) {
//                log.info("Client:{}, WRITER_IDLE 写超时",socketString);
//                ctx.disconnect();
//                Channel channel = ctx.channel();
//                ChannelId id = channel.id();
//                ChannelMap.removeChannelByName(id);
//            } else if (event.state() == IdleState.ALL_IDLE) {
//                log.info("Client:{},ALL_IDLE 总超时",socketString);
//                ctx.disconnect();
//                Channel channel = ctx.channel();
//                ChannelId id = channel.id();
//                ChannelMap.removeChannelByName(id);
//            }
        IdleStateEvent event = (IdleStateEvent) evt;
        // 触发了读空闲事件
        if (event.state() == IdleState.READER_IDLE) {
//           log.info("已经五秒没有发消息了");
            List<RegisterMessage> list = registerMessageServiceStatic.list(new QueryWrapper<RegisterMessage>().lambda()
                    .eq(RegisterMessage::getChannelId, ctx.channel().id().toString())
                    .eq(RegisterMessage::getStatus, 1)
            );
            if (!CollectionUtils.isEmpty(list)) {
                RegisterMessage registerMessage = list.stream().findFirst().orElse(null);
                registerMessage.setStatus(0);
                registerMessageServiceStatic.updateById(registerMessage);
            }
            ctx.channel().close();
        }

    }


    /**
     * 功能描述: 发生异常会触发此函数
     * @Author sxf
     * @Date 2022/8/26
     * @param  ctx 通道处理程序上下文
     * @param  cause 异常
     * @return void
     */
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
//        ctx.close();
//        log.info("{}:发生了错误,此连接被关闭。此时连通数量:{}",ctx.channel().id(),ChannelMap.getChannelMap().size());
//    }
}
