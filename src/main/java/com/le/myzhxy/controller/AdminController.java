package com.le.myzhxy.controller;


import com.le.myzhxy.pojo.Admin;
import com.le.myzhxy.service.AdminService;
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

@Api(tags = "管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {
    //@Autowired(required=true)
    @Autowired
    private AdminService adminService ;

    // GET
    //	http://localhost:9002/sms/adminController/getAllAdmin/1/3?    adminName=a
    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("管理员名字") String adminName
    ){
        Page<Admin> pageParam =new Page<Admin>(pageNo,pageSize);

        IPage<Admin> iPage=adminService.getAdminsByOpr(pageParam,adminName);
        return Result.ok(iPage);
    }

    //POST
    //	http://localhost:9002/sms/adminController/saveOrUpdateAdmin  admin
    @ApiOperation("增加或修改管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result  saveOrUpdateAdmin(
            @ApiParam("JSON格式的Admin对象") @RequestBody Admin admin
    ){
        Integer id = admin.getId();
        if (id==null || 0 ==id) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();

    }

    // DELETE
    //	http://localhost:9002/sms/adminController/deleteAdmin List<Integer> ids
    @ApiOperation("删除单个或者多个管理员信息")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(
           @ApiParam("要删除的管理员的多个ID的JSON集合") @RequestBody List<Integer> ids
    ){
        adminService.removeByIds(ids);
        return Result.ok();
    }



}
