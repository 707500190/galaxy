package com.sun.content.api.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/16
 */
@Data
public class CrawlerBaseDTO {

    private Long id;
    @ApiModelProperty(value = "账号, ','相隔的多个账号")
    private String account;
    @ApiModelProperty(value = "渠道")
    private String channel;
    @ApiModelProperty(value = "标签")
    private List<String> accountTag;
    @ApiModelProperty(value = "异常状态")
    private String status;
}
