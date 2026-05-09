package com.zff.springboot_demo.file.controller;

import com.zff.springboot_demo.Result;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件管理接口
 */
@RestController
@RequestMapping("/api/file")
public class FileController {


    /**
     * 上传文件并返回访问路径
     * @param file 上传的文件
     * @return 文件访问路径
     */
    @PostMapping("/upload")
    public Result<String> upload(@RequestParam("file") MultipartFile file) {
        // 你来实现
        String originalName = file.getOriginalFilename(); // 拿原始文件名
        String suffix = originalName.substring(originalName.lastIndexOf(".")); // 截取后缀
        String newName = UUID.randomUUID().toString() + suffix; // 拼成新文件名
        File dir = new File(System.getProperty("user.dir"), "uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(dir, newName));
        } catch (IOException e) {
            return Result.error(e.getMessage());
        }
        return  Result.success("upload success","/uploads/" + newName);
    }
}
