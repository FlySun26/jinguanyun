package com.txc.mybatis.controller;

import com.alibaba.fastjson.JSON;
import com.txc.mybatis.bean.RestartMessageCode;
import com.txc.mybatis.bean.SettingCurrentMessageCode;
import com.txc.mybatis.bean.StartMessageCodeMessage;
import com.txc.mybatis.buffer.HeartbeatBufferImpl;
import com.txc.mybatis.service.ChannelMap;
import com.txc.mybatis.service.RegisterRequestMessage;
import com.txc.mybatis.service.RegisterMessageService;
import com.txc.mybatis.service.StudentService;
import io.netty.channel.Channel;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@Api(tags = "消息视图层",value = "消息视图层")
public class StartMessageController {

    @Resource
    private RabbitTemplate rabbitTemplate;
    @Autowired
    StudentService studentService;

    @Autowired
    RegisterMessageService registerMessageService;

    @Autowired
    RedisTemplate redisTemplate;

    @Resource
    private HeartbeatBufferImpl heartbeatBuffer;

    @GetMapping("/findAll")
    public String findAll(){
//        String key = ChannelMap.getChannelMap().keys().nextElement();
//        Channel fff = ChannelMap.getChannelByName(key);
//
//
        RegisterRequestMessage registerMessage = new RegisterRequestMessage();
        registerMessage.setCode("wulalawulawulala");
        registerMessage.setMessageType(501);
        registerMessage.setDevAddr(128893033);
//        fff.writeAndFlush(registerMessage);

        rabbitTemplate.convertAndSend("jinguanyun","",registerMessage);

//        JedisUtil.set("hengheng", "dudu");
//        System.out.println(JedisUtil.get("hengheng"));
//        List<Student> list= studentService.findAll();
//        registerMessageService.list();
//        System.out.println("=====总数据条数===="+list.size());
        return "index";//视图信息
    }

    @PostMapping(value = "/startMessagePush")
    @ApiOperation(value = "开机消息下发")
    public String startMessagePush(@RequestBody RegisterRequestMessage message) {
        StartMessageCodeMessage codeMessage = JSON.parseObject(JSON.toJSONString(message.getObject()), StartMessageCodeMessage.class);
        message.setObject(codeMessage);
        rabbitTemplate.convertAndSend("jinguanyun","StartMessageRouting",message);
        return "success";
    }

    @PostMapping(value = "/endMessagePush")
    @ApiOperation(value = "关机消息下发")
    public String endMessagePush(@RequestBody RegisterRequestMessage message) {
        StartMessageCodeMessage codeMessage = JSON.parseObject(JSON.toJSONString(message.getObject()), StartMessageCodeMessage.class);
        message.setObject(codeMessage);
        rabbitTemplate.convertAndSend("jinguanyun","StartMessageRouting",message);
        return "success";
    }


    @PostMapping(value = "/settingCurrent")
    @ApiOperation(value = "设置电流消息下发")
    public String settingCurrent(@RequestBody RegisterRequestMessage message) {
        SettingCurrentMessageCode codeMessage = JSON.parseObject(JSON.toJSONString(message.getObject()), SettingCurrentMessageCode.class);
        message.setObject(codeMessage);
        rabbitTemplate.convertAndSend("jinguanyun","StartMessageRouting",message);
        return "success";
    }

    @PostMapping(value = "/restartMessagePush")
    @ApiOperation(value = "重启重新桩指令下发")
    public String restartMessagePush(@RequestBody RegisterRequestMessage message) {
        RestartMessageCode codeMessage = JSON.parseObject(JSON.toJSONString(message.getObject()), RestartMessageCode.class);
        message.setObject(codeMessage);
        rabbitTemplate.convertAndSend("jinguanyun","StartMessageRouting",message);
        return "success";
    }

}
