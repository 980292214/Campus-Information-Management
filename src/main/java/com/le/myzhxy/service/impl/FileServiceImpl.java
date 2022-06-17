package com.le.myzhxy.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.le.myzhxy.service.FileService;
import com.le.myzhxy.util.ConstantOssPropertiesUtils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

/**
 * @author 乐
 * @version 1.0
 */
@Service
public class FileServiceImpl implements FileService {
    @Override
    public String upload(MultipartFile multipartFile) {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = ConstantOssPropertiesUtils.ENDPOINT;
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = ConstantOssPropertiesUtils.ACCESS_KEY_ID;//子账号
        String accessKeySecret = ConstantOssPropertiesUtils.SECRECT;
        // 填写Bucket名称，例如examplebucket。
        String bucketName = ConstantOssPropertiesUtils.BUCKET;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        try {
            //上传文件流
            InputStream inputStream = multipartFile.getInputStream();
            String fileName = multipartFile.getOriginalFilename();
            //生成随机唯一值，使用uuid，添加到文件名称里面
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            fileName =uuid+fileName;
            //按照当前日期，创建文件夹，上传到创建文件夹里面
            String timeUrl = new DateTime().toString("yyyy/MM/dd");
            fileName=timeUrl+"/"+fileName;
            ossClient.putObject(bucketName, fileName, inputStream);
            //上传之后文件路径
            String url = "https://"+bucketName+"."+endpoint+"/"+fileName;
            System.out.println("上传头像成功");
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
