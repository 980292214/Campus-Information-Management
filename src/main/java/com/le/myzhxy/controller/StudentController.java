package com.le.myzhxy.controller;

import com.le.myzhxy.pojo.Student;
import com.le.myzhxy.service.StudentService;
import com.le.myzhxy.util.MD5;
import com.le.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生控制器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {
    @Autowired
    private StudentService studentService;
    // DELETE
    //	http://localhost:9001/sms/studentController/delStudentById
    @ApiOperation("删除单个或者多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(
          @ApiParam("要删除的学生编号的JSON数组") @RequestBody List<Integer> ids
    ){
        studentService.removeByIds(ids);
        return Result.ok();
    }


    // POST  http://localhost:9002/sms/studentController/addOrUpdateStudent

    @ApiOperation("保存或者修改学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(
            @ApiParam("要保存或修改的学生JSON")@RequestBody  Student student
    ){
        Integer id = student.getId();
        if (null == id || 0 ==id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }



    //GET /sms/studentController/getStudentByOpr/1/3?name=&clazzName=

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentBuOpr(
        @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
        @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
        @ApiParam("查询的条件") Student student
    ){
        //分页信息封装Page对象
        Page<Student> pageParam =new Page(pageNo,pageSize);
        // 进行查询
        IPage<Student> studentPage =studentService.getStudentByOpr(pageParam,student);
        // 封装Result返回
        return Result.ok(studentPage);
    }


}
