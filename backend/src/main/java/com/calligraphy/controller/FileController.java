package com.calligraphy.controller;

import com.calligraphy.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api/file")
public class FileController {

    @Value("${file.upload.path}")
    private String uploadPath;

    private String absoluteUploadPath;

    @PostConstruct
    public void init() {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.isAbsolute()) {
            uploadDir = new File(System.getProperty("user.dir"), uploadPath);
        }
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        absoluteUploadPath = uploadDir.getAbsolutePath() + File.separator;
    }

    @PostMapping("/upload")
    public Result<?> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Result.error("文件不能为空");
        }
        String originalFilename = file.getOriginalFilename();
        String suffix = (originalFilename != null && originalFilename.lastIndexOf(".") > 0)
                ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String fileName = UUID.randomUUID().toString() + suffix;
        File dest = new File(absoluteUploadPath + fileName);
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            return Result.error("文件上传失败: " + e.getMessage());
        }
        return Result.success("/upload/" + fileName);
    }
}
