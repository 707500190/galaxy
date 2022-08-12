package com.sun.content.api.dto;

import lombok.Data;

import java.util.List;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/24
 */
@Data
public class ContentSyncDTO {

    private List<Long> ids;

    private String channel;

    private SyncTypeEnum syncType;


}
