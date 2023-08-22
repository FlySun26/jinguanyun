package com.txc.mybatis.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txc.mybatis.bean.RegisterMessage;
import com.txc.mybatis.mapper.RegisterMessageMapper;
import io.netty.buffer.ByteBuf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName RegisterMessageService
 * @Description TODO
 * @Date 2023/8/17 10:00
 * @Vertion 1.0
 **/
@Service
@Slf4j
public class RegisterMessageService extends ServiceImpl<RegisterMessageMapper, RegisterMessage> {


    public void encode(ByteBuf out, Message msg, List<Object> outList) {

    }

    public void decode(ByteBuf out, Message msg, List<Object> outList) {

    }


}
