package com.txc.mybatis.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.txc.mybatis.bean.Student;

import java.util.List;

public interface StudentMapper extends BaseMapper<Student> {
    //查询全部的学生信息
    public List<Student> findAll();

    //查询全部的学生信息
    public List<Student> findStudentById(String stu_id);

    //添加学生信息
    public void addStudent(Student stu);

    //通过id修改学生信息
    public void updStudentById(Student stu);

    //通过id删除学生信息
    public  void  deleteStuentById(String stu_id);

}
