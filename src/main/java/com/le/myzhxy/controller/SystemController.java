package com.le.myzhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.le.myzhxy.pojo.Admin;
import com.le.myzhxy.pojo.LoginForm;
import com.le.myzhxy.pojo.Student;
import com.le.myzhxy.pojo.Teacher;
import com.le.myzhxy.service.AdminService;
import com.le.myzhxy.service.FileService;
import com.le.myzhxy.service.StudentService;
import com.le.myzhxy.service.TeacherService;
import com.le.myzhxy.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private FileService fileService;

    /*

    * 修改密码的处理器
    * POST  /sms/system/updatePwd/123456/admin
    *       /sms/system/updatePwd/{oldPwd}/{newPwd}
    *       请求参数
                oldpwd
                newPwd
                token 头
            响应的数据
                Result OK data= null

    * */

    @ApiOperation("更新用户密码的处理器")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            // token过期
            return Result.fail().message("token失效,请重新登录后修改密码");
        }
        // 获取用户ID和用类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", userId.intValue());
                queryWrapper1.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin != null) {
                    // 修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有误!");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", userId.intValue());
                queryWrapper2.eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student != null) {
                    // 修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有误!");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id", userId.intValue());
                queryWrapper3.eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher != null) {
                    // 修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有误!");
                }
                break;

        }
        return Result.ok();
    }


//    // POST /sms/system/headerImgUpload
//    @ApiOperation("文件上传统一入口,上传到本地")
//    @PostMapping("/headerImgUpload")
//    public Result headerImgUpload(
//            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile
//            ,
//            HttpServletRequest request
//    ) {
//
//        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
//        String originalFilename = multipartFile.getOriginalFilename();
//        int i = originalFilename.lastIndexOf(".");
//        String newFileName = uuid.concat(originalFilename.substring(i));//.concat就是+
//
//        // 保存文件 将文件发送到第三方/独立的图片服务器上,--6.16
//        String portraitPath = "E:\\学习\\尚硅谷\\代码文件\\2022智慧校园\\target\\classes\\public\\upload\\".concat(newFileName);
//        try {
//            multipartFile.transferTo(new File(portraitPath));
//        } catch (IOException e) {
//            System.out.println("上传头像失败！");
//            e.printStackTrace();
//        }
//        // 响应图片的路径
//        String path = "upload/".concat(newFileName);
//        return Result.ok(path);
//    }

    // POST /sms/system/headerImgUpload
    @ApiOperation("文件上传统一入口,上传到阿里云")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("头像文件") @RequestPart("multipartFile") MultipartFile multipartFile
    ) {
        String url = fileService.upload(multipartFile);
        // 响应图片的路径
        return Result.ok(url);
    }


    @ApiOperation("通过token口令获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(
            @ApiParam("token口令") @RequestHeader("token") String token) {
        //1.判断是否已过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        //2.从token中解析出 用户id 和用户的类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);


        Map<String, Object> map = new LinkedHashMap<>();
        //3.根据 用户id 和 用户类型 返回对应的数据
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }


    @ApiOperation("登录的方法")
    @PostMapping("/login")
    public Result login(
            @ApiParam("登录提交信息的form表单") @RequestBody LoginForm loginForm,
            HttpServletRequest request) {
        // 验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码失效,请刷新后重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码有误,请小心输入后重试");
        }
        // 从session域中移除现有验证码
        session.removeAttribute("verifiCode");
        // 分用户类型进行校验--


        // 准备一个map用户存放响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        // 用户的类型和用户id转换成一个密文,以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        // 用户的类型和用户id转换成一个密文,以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名或者密码有误！！！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teahcer = teacherService.login(loginForm);
                    if (null != teahcer) {
                        // 用户的类型和用户id转换成一个密文,以token的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(teahcer.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名或者密码有误");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");

    }


    @ApiOperation("获取验证码图片")//--
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 1.获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        // 2.获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        // 3.将验证码文本放入session域,为下一次验证做准备
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        // 4.将验证码图片响应给浏览器

        try {
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
