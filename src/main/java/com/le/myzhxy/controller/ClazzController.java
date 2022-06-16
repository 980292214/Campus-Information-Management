package com.le.myzhxy.controller;

import com.le.myzhxy.pojo.Clazz;
import com.le.myzhxy.service.ClazzService;
import com.le.myzhxy.util.Result;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    private ClazzService clazzService;


    //GET /sms/clazzController/getClazzs

    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzes= clazzService.getClazzs();
        return Result.ok(clazzes);
    }



    //DELETE sms/clazzController/deleteClazz  [1,2,3]
    @ApiOperation("删除单个和多个班级")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(
           @ApiParam("要删除的多个班级的ID的JSON数组")@RequestBody List<Integer> ids
    ){
        clazzService.removeByIds(ids);

        return Result.ok();
    }



    //	/sms/clazzController/saveOrUpdateClazz


    @ApiOperation("增加或者修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(
            @ApiParam("JSON格式的班级信息")@RequestBody Clazz clazz
    ){
        clazzService.saveOrUpdate(clazz);//主键有值是修改，无则是增加！6.15
        return Result.ok();
    }



    // sms/clazzController/getClazzsByOpr/1/3?gradeName=&name=

    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("分页查询的页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("分页查询页大小")@PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Clazz clazz //和传过来的同名
    ){
        Page<Clazz> page =new Page<>(pageNo,pageSize);

        IPage<Clazz> iPage=clazzService.getClazzsByOpr(page,clazz);

        return Result.ok(iPage);

    }

}
