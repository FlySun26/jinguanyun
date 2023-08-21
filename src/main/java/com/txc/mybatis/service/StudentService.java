package com.txc.mybatis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.txc.mybatis.bean.Student;
import com.txc.mybatis.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {
    @Autowired(required = false)
    StudentMapper studentMapper;



    //查询全部的学生信息
    public List<Student> findAll() {


        return studentMapper.findAll();
    }

    //查询全部的学生信息
    public List<Student> findStudentById(String stu_id) {
        return studentMapper.findStudentById(stu_id);
    }

    //添加学生信息
    public void addStudent(Student stu) {
        studentMapper.addStudent(stu);
    }

    //通过id修改学生信息
    public void updStudentById(Student stu) {

        studentMapper.updStudentById(stu);
    }

    //通过id删除学生信息
    public void deleteStuentById(String stu_id) {
        studentMapper.deleteStuentById(stu_id);
    }
}
