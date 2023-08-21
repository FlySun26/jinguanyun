package com.txc.mybatis.service;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(callSuper = true)
public class RegisterRequestMessage extends Message {
    private String code;

    public RegisterRequestMessage(String code) {
        this.code = code;
    }

    public RegisterRequestMessage() {
    }

//    @Override
//    public int getMessageType() {
//        return 101;
//    }
}
