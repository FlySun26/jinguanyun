package com.txc.mybatis.bean;

import com.txc.mybatis.util.CRCUtil;
import lombok.Data;

/**
 * @ClassName FailStartMessage
 * @Description TODO
 * @Date 2023/8/25 16:57
 * @Vertion 1.0
 **/
@Data
public class FailStartMessage {

    //充电枪编号
    private Integer spearNum;
    //交易流水号
    private String transactionSerialNum;
    //故障码
    private Integer errorCode;
    //参数1
    private Integer param1;
    //参数2
    private Integer param2;
}
