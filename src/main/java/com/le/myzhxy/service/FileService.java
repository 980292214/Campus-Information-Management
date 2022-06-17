package com.le.myzhxy.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author 乐
 * @version 1.0
 */
public interface FileService {
    //上传文件到阿里云
    String upload(MultipartFile multipartFile);

}
