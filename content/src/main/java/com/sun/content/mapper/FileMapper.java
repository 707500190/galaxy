package com.sun.content.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sun.content.api.entity.FileInfo;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface FileMapper extends BaseMapper<FileInfo> {


    /**
     * 插入一行数据
     *
     * @param fileInfo
     * @return
     */
    int save(FileInfo fileInfo);


}
