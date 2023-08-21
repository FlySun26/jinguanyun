package com.txc.mybatis.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName RegisterMessage
 * @Description TODO
 * @Date 2023/8/17 09:54
 * @Vertion 1.0
 **/
@Data
@TableName("register_message")
public class RegisterMessage {


    private Integer id;


    private Integer devAddr;

    private Integer status;

    private String channelId;
}
