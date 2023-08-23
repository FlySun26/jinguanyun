package com.txc.mybatis.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName ErrorSpear
 * @Description TODO
 * @Date 2023/8/23 08:38
 * @Vertion 1.0
 **/
@Data
@TableName("error_spear")
public class ErrorSpear extends BaseEntity {


    private Integer spearCode;


    private Integer status;

    private Integer errorCode;

    private Integer devAddr;

    /**
     * 充电电压/故障代码/几点
     */
    private Integer param1;


    /**
     * 充电电流/参数1/几分
     */
    private Integer param2;
}
