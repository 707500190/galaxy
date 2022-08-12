package com.sun.content.api.common.utils;

import com.sun.content.api.entity.ContentInfo;
import com.sun.content.api.entity.EsSyncable;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.security.MessageDigest;

/**
 * @author sunshilong
 * @version 1.0
 * @date 2022/3/8
 */
public class ESUtils {

    /**
     * 获取ES ID(废弃：无法过滤重复的内容)
     *
     * @param channel 渠道
     * @param id      dbID
     * @return 返回全局ESId
     */
    @Deprecated
    public static String getContentId(String channel, Long id) {
        return String.format("%s_%s", channel, id);
    }


    public static <T extends EsSyncable> String getESId(T s) {
        ContentInfo content = new ContentInfo();
        BeanUtils.copyProperties(s, content);
        Assert.notNull(content, "getESId error: content is null");
        String key = content.getAccount() + content.getChannel() + content.getTitle();
        String id = toMD5(key);
//        Assert.notEmpty(id, "getESId is error, id is null ");
        return id;
    }

    public static String toMD5(String text) {
        //生成实现指定摘要算法的 MessageDigest 对象。
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            //使用指定的字节数组更新摘要。
            md.update(text.getBytes());
            //通过执行诸如填充之类的最终操作完成哈希计算。
            byte b[] = md.digest();
            //生成具体的md5密码到buf数组
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
