package com.sun.content.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.content.api.entity.FileInfo;
import com.sun.content.config.UploadConfig;
import com.sun.content.mapper.FileMapper;
import com.sun.content.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import static com.sun.content.api.common.constant.Constant.DOU_YIN;
import static com.sun.content.util.FileUtils.generateFileName;
import static com.sun.content.util.UploadUtils.*;


/**
 * 文件上传服务
 */
@Service

@RequiredArgsConstructor
@Slf4j
public class FileService extends ServiceImpl<FileMapper, FileInfo> {

    private final FileMapper fileMapper;

    private static ReentrantLock lock = new ReentrantLock();

    private final StringRedisTemplate redisTemplate;

    private static final String DOWNLOAD_LOCK_PREFIX = "download:url:";

    /**
     * 上传文件
     *
     * @param md5
     * @param file
     */
    public String upload(String name,
                         String md5,
                         MultipartFile file, String channel) throws IOException {
        String ext = FileUtils.getExt(file);
        String basePath = UploadConfig.path;
        if (DOU_YIN.equals(channel)) {
            basePath = UploadConfig.path + "douyin";
        }
        //创建临时目录
        File fileInfoInfoDir = new File(basePath);
        String filePath = basePath + File.separator + generateFileName(ext);
        log.info("filePath is {}", filePath);
        //创建文件夹
        if (!fileInfoInfoDir.exists()) {
            fileInfoInfoDir.mkdirs();
            log.info("basePath director be created! basePath: {}", basePath);
        }
        log.info( "path is Null ! name is : {}", name);

        FileUtils.write(filePath, file.getInputStream());
        fileMapper.save(new FileInfo(name, md5, filePath, new Date(), ext));
        return filePath;
    }

    /**
     * 分块上传文件
     *
     * @param md5
     * @param size
     * @param chunks
     * @param chunk
     * @param file
     * @throws IOException
     */
    public void uploadWithBlock(String name,
                                String md5,
                                Long size,
                                Integer chunks,
                                Integer chunk,
                                MultipartFile file) throws IOException {
        String fileName = getFileName(md5, chunks);
        FileUtils.writeWithBlok(UploadConfig.path + fileName, size, file.getInputStream(), file.getSize(), chunks, chunk);
        addChunk(md5, chunk);
        if (isUploaded(md5)) {
            removeKey(md5);
            fileMapper.save(new FileInfo(name, md5, UploadConfig.path + fileName, new Date(), FileUtils.getExt(file)));
        }
    }

    /**
     * 检查Md5判断文件是否已上传
     * true:  未上传
     * false: 已上传
     *
     * @param md5
     * @return
     */
    public boolean checkMd5(String md5) {
        FileInfo fileInfo = new FileInfo();
        fileInfo.setMd5(md5);
        QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
        return this.getOne(wrapper) == null;
    }

    /**
     * 通过Url返回存储位置；
     *
     * @param url 资源url
     * @return 服务器存储位置；
     */
    public String downloadByUrl(String url, String ext) {

        if (StringUtils.isEmpty(url)) {
            log.error("download originUrl is empty !");
            return "";
        }
        //先判断该资源是否已存在，是则直接返回url
        String storePath = getPathRedis(url);
        if (StringUtils.isNotEmpty(storePath)) {
            return storePath;
        }
        String urlKey = DOWNLOAD_LOCK_PREFIX + url;
        //对于同一个url的下载加锁，防止表记录下载多个重复文件,造成资源浪费
//        redisTemplate.opsForValue().setIfAbsent()
        synchronized (url.intern()) {
            //先从redis中取
            storePath = redisTemplate.opsForValue().get(urlKey);
            if (StringUtils.isNotEmpty(storePath)) {
                return storePath;
            }
            QueryWrapper<FileInfo> wrapper = new QueryWrapper<>();
            wrapper.eq("origin_url", url);
            List<FileInfo> resList = this.list(wrapper);
            if (!CollectionUtils.isEmpty(resList)) {
                storePath = resList.get(0).getPath();
                redisTemplate.opsForValue().set(urlKey, storePath);
                return storePath;
            }
            //资源不存在，下载资源
            String filePath = UploadConfig.path;
            String name = generateFileName() + "." + ext;

            log.info("filePath is {}", filePath);
            String md5 = FileUtils.downLoadByUrl(url, name, filePath);
            storePath = filePath + name;
            //存储成功，入库记录
            if (StringUtils.isNotEmpty(md5)) {
                FileInfo file = new FileInfo(name, md5, filePath, new Date(), ext, url);
                this.save(file);
                redisTemplate.opsForValue().set(urlKey, storePath);
                return storePath;
            }
        }
        //存储失败返回空串
        return "";
    }

    private String getPathRedis(String key) {
        //先判断该资源是否已存在，是则直接返回url
        String redisUrl = redisTemplate.opsForValue().get(DOWNLOAD_LOCK_PREFIX + key);
        return redisUrl;
    }
}
