package com.le.myzhxy.service.impl;

import com.le.myzhxy.mapper.StudentMapper;
import com.le.myzhxy.pojo.LoginForm;
import com.le.myzhxy.pojo.Student;
import com.le.myzhxy.service.StudentService;
import com.le.myzhxy.util.MD5;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**

 */
@Service("stuService")
@Transactional
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public Student login(LoginForm loginForm) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<>();
        QueryWrapper<Student> queryWrapper2=new QueryWrapper<>();
        //更新学号登录6.16
        String username = loginForm.getUsername();
        queryWrapper.eq("sno",username).or().eq("name",username);//生成的sql语句不同6.16
        //queryWrapper.and(wrapper->wrapper.eq("sno",username).or().eq("name",username));
        queryWrapper.eq("password",MD5.encrypt(loginForm.getPassword()));

        Student student = baseMapper.selectOne(queryWrapper);

        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        QueryWrapper<Student> queryWrapper=new QueryWrapper<Student>();
        queryWrapper.eq("id",userId);
        return baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        QueryWrapper<Student> studentQueryWrapper =new QueryWrapper<>();
        if(!StringUtils.isEmpty(student.getName())){
            studentQueryWrapper.like("name",student.getName());
        }
        if(!StringUtils.isEmpty(student.getClazzName())){
            studentQueryWrapper.like("clazz_name",student.getClazzName());
        }
        studentQueryWrapper.orderByDesc("id");
        Page<Student> studentPage = baseMapper.selectPage(pageParam, studentQueryWrapper);
        return studentPage;
    }


}
