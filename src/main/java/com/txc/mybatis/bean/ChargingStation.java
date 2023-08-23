package com.txc.mybatis.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ChargingStation
 * @Description TODO
 * @Date 2023/8/23 09:51
 * @Vertion 1.0
 **/
@Data
@TableName("charging_station")
public class ChargingStation {

    private Integer id;

    private Integer devAddr;

    private Integer deleteFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
