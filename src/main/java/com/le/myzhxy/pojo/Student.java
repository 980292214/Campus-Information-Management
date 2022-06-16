package com.le.myzhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @project: sms
 * @description: 学生信息
 */
@Data
@TableName("tb_student")
public class Student {

    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String sno;
    private String name;
    private char gender = '男';//default
    private String password;
    private String email;
    private String telephone;
    private String address;
    private String introducation;
    private String portraitPath;//存储头像的项目路径
    private String clazzName;//班级名称

}
