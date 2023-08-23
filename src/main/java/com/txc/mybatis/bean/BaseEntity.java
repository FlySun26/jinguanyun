package com.txc.mybatis.bean;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName BaseEntity
 * @Description TODO
 * @Date 2023/8/23 08:38
 * @Vertion 1.0
 **/
@Data
public class BaseEntity {

    private Integer id;

    private Integer deleteFlag;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
