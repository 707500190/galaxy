package com.sun.content.template;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sun.content.api.dto.QueryContentDTO;
import com.sun.content.api.entity.Tiktok;
import com.sun.content.service.FileService;
import com.sun.content.service.TiktokService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import static com.sun.content.api.common.constant.Constant.DOU_YIN;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/5/20
 */
@Component(DOU_YIN)
@RequiredArgsConstructor
@Slf4j
public class TiktokContentTemplate extends AbstractContentQueryTemplate<Tiktok>{

    private final FileService fileService;
    private final TiktokService tiktokService;

    @Override
    protected IService<Tiktok> getQueryService() {
        return tiktokService;
    }

    @Override
    protected Class<Tiktok> getType() {
        return Tiktok.class;
    }

    /**
     * 抖音视频具有时效性，将视频进行下载，
     * 返回下载的路径存储到ES;
     *
     * @param tik DB获取到的对象
     * @param res parse的result
     */
    @Override
    void parse(Tiktok tik, QueryContentDTO res){
        String url = tik.getVideoUrl();
        //根据Url下载 抖音视频
        String ext = getExt(url);
        String path = fileService.downloadByUrl(url, ext);
        log.info("Tiktok resource path is {}:", path);
        res.setVideoPath(path);
        res.setArticleLikeNumber(tik.getLikeNumber());
    }

    private String getExt(String originUrl){

        if (StringUtils.contains(originUrl,"mp3")){
            return "mp3";
        }
        else if (StringUtils.contains(originUrl,"mp4")) {
            return "mp4";
        }
        else {
            return "mp4";
        }
    }
}
