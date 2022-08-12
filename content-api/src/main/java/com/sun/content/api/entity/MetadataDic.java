package com.sun.content.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @描述：
 * @作者: sunshilong
 * @日期: 2022-03-14
 */

@Data
@ApiModel(value="")
@TableName("metadata_dic")
public class MetadataDic {

    private static final long serialVersionUID=1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Long id;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "code")
    private String code;
    @ApiModelProperty(value = "类型")
    private String type;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "描述")
    private String description;
}