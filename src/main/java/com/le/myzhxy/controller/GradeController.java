package com.le.myzhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.le.myzhxy.pojo.Grade;
import com.le.myzhxy.service.GradeService;
import com.le.myzhxy.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    // /sms/gradeController/getGrades


    @ApiOperation("获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades() {
        List<Grade> grades = gradeService.getGrades();
        //List<Grade> list = gradeService.list();
        return Result.ok(grades);

    }


    //sms/gradeController/deleteGrade
    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(
            @ApiParam("要删除的所有的grade的id的JSON集合") @RequestBody List<Integer> ids) {

        gradeService.removeByIds(ids);
        boolean b = gradeService.removeByIds(ids);
        if (!b) return Result.fail().message("删除失败!");

        return Result.ok();

    }

    @ApiOperation("新增或修改grade,有id属性是修改,没有则是增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(
            @ApiParam("JSON的Grade对象") @RequestBody Grade grade
    ) {
        // 接收参数
        // 调用服务层方法完成增减或者修改
        //gradeService.saveOrUpdate(grade);
        boolean b = gradeService.saveOrUpdate(grade);
        if (!b) return Result.fail().message("添加/更新失败!");

        return Result.ok();
    }


    //sms/gradeController/getGrades/1/3?gradeName=%E4%B8%89

    @ApiOperation("根据年级名称模糊查询,带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询的页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询模糊匹配的名称") String gradeName//@RequestParam("gradeName") 写了就得传，不然报错6.14

    ) {
        // 分页 带条件查询
        Page<Grade> page = new Page<>(pageNo, pageSize);
        // 通过服务层
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);

        // 封装Result对象并返回
        return Result.ok(pageRs);
    }
}
