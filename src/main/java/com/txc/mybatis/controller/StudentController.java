package com.txc.mybatis.controller;

import com.txc.mybatis.bean.Student;
import com.txc.mybatis.util.JedisUtil;
import com.txc.mybatis.service.RegisterMessageService;
import com.txc.mybatis.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    RegisterMessageService registerMessageService;

    @Autowired
    RedisTemplate redisTemplate;

    @RequestMapping("/findAll")
    public String findAll(){

        JedisUtil.set("hengheng", "dudu");
        System.out.println(JedisUtil.get("hengheng"));
        List<Student> list= studentService.findAll();
        registerMessageService.list();
        System.out.println("=====总数据条数===="+list.size());
        return "index";//视图信息
    }

}
