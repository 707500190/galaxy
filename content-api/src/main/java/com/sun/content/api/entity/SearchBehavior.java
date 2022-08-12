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
 * @日期: 2022-05-07
 */

@Data
@ApiModel(value = "")
@TableName("search_behavior")
public class SearchBehavior {

    private static final long serialVersionUID = 1L;

    public SearchBehavior(){

    }
    public SearchBehavior(String value, String type){
        this.type = type;
        this.value = value;
        this.createTime = new Date();
    }

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "")
    private Long id;
    @ApiModelProperty(value = "")
    private String value;
    @ApiModelProperty(value = "")
    private String type;
    @ApiModelProperty(value = "")
    private Date createTime;
}