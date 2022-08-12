package com.sun.content.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * @描述：
 * @作者: sunshilong
 * @日期: 2022-05-23
 */

@Data
@ApiModel(value = "")
@TableName("file_info")
@AllArgsConstructor
public class FileInfo {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "文件名")
    private String name;
    @ApiModelProperty(value = "MD5值")
    private String md5;
    @ApiModelProperty(value = "文件路径")
    private String path;
    @ApiModelProperty(value = "上传时间")
    private Date uploadTime;
    @ApiModelProperty(value = "文件后缀名")
    private String ext;
    @ApiModelProperty(value = "资源，源路径")
    private String originUrl;

    public FileInfo() {
    }

    public FileInfo(String name, String md5, String filePath, Date date, String ext) {
        this.md5 = (md5);
        this.name = name;
        this.path = filePath;
        this.uploadTime = date;
        this.ext = ext;
    }

    public FileInfo(String name, String md5, String filePath, Date date, String ext, String originUrl) {
        this.md5 = (md5);
        this.name = name;
        this.path = filePath;
        this.uploadTime = date;
        this.ext = ext;
        this.originUrl = originUrl;
    }
}