package com.txc.mybatis.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.txc.mybatis.bean.ChargingStation;
import com.txc.mybatis.bean.RegisterMessage;
import com.txc.mybatis.service.ChannelMap;
import com.txc.mybatis.service.ChargingStationService;
import com.txc.mybatis.service.RegisterMessageService;
import com.txc.mybatis.service.RegisterRequestMessage;
import io.netty.channel.Channel;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName TestRabbitService
 * @Description TODO
 * @Date 2023/8/28 16:37
 * @Vertion 1.0
 **/

@RabbitListener(bindings = @QueueBinding(
        // email.fanout.queue 是队列名字，这个名字你可以自定随便定义。
        value = @Queue(value = "StartMessageDirectQueue", autoDelete = "false"),
        // order.fanout 交换机的名字 必须和生产者保持一致
        exchange = @Exchange(value = "jinguanyun",
                // 这里是确定的rabbitmq模式是：fanout 是以广播模式 、 发布订阅模式
                type = ExchangeTypes.DIRECT)
))
@Component
public class TestRabbitService {

    @Resource
    private ChargingStationService chargingStationService;
    @Resource
    private RegisterMessageService registerMessageService;

    // @RabbitHandler 代表此方法是一个消息接收的方法。该不要有返回值
    @RabbitHandler
    public void messagerevice(RegisterRequestMessage message) {
        // 此处省略发邮件的逻辑
        System.out.println("email-------------->" + message);
        List<RegisterMessage> list1 = registerMessageService.list(new QueryWrapper<RegisterMessage>().lambda().eq(RegisterMessage::getDevAddr, message.getDevAddr()));
        RegisterMessage registerMessage = list1.stream().findFirst().orElse(null);
        message.setChannelId(registerMessage.getChannelId());
        Channel channel = ChannelMap.getChannelByName(message.getChannelId());
        channel.writeAndFlush(message);
    }


}
