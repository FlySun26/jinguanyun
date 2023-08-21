package com.txc.mybatis.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@TableName()
public class Student {
    private String stu_id;
    private String stu_name;
    private String stu_sex;
    private String stu_age;
    private String stu_addr;
    private String stu_pwd;
}
