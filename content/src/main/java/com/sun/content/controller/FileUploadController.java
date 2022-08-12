package com.sun.content.controller;

import com.sun.content.service.FileService;
import com.sun.content.api.common.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

/**
 * 文件上传
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
@Api(tags = "文件上传相关接口")

@RequiredArgsConstructor
public class FileUploadController {

    private final FileService fileService;

    @ApiOperation("文件上传接口")
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file, String channel) throws IOException {
        if (Objects.isNull(file)) {
           return R.failed("文件不可为空！");
        }
        String url = fileService.upload(file.getOriginalFilename(), DigestUtils.md5Hex(file.getBytes()), file, channel);
        return R.ok(url, "success");
    }
}
