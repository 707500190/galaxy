package com.sun.content.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * 文件操作工具类
 */
@Slf4j
public class FileUtils {

    /**
     * 写入文件
     *
     * @param target
     * @param src
     * @throws IOException
     */
    public static void write(String target, InputStream src) throws IOException {
        OutputStream os = new FileOutputStream(target);
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = src.read(buf))) {
            os.write(buf, 0, len);
        }
        os.flush();
        os.close();
    }

    /**
     * 分块写入文件
     *
     * @param target
     * @param targetSize
     * @param src
     * @param srcSize
     * @param chunks
     * @param chunk
     * @throws IOException
     */
    public static void writeWithBlok(String target, Long targetSize, InputStream src, Long srcSize, Integer chunks, Integer chunk) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(target, "rw");
        randomAccessFile.setLength(targetSize);
        if (chunk == chunks - 1 && chunk != 0) {
            randomAccessFile.seek(chunk * (targetSize - srcSize) / chunk);
        } else {
            randomAccessFile.seek(chunk * srcSize);
        }
        byte[] buf = new byte[1024];
        int len;
        while (-1 != (len = src.read(buf))) {
            randomAccessFile.write(buf, 0, len);
        }
        randomAccessFile.close();
    }

    /**
     * 生成随机文件名
     *
     * @return
     */
    public static String generateFileName(String ext) {
        String uid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        if (StringUtils.isNotEmpty(ext)) {
            uid = uid + "." + ext;
        }
        return uid;
    }

    public static String generateFileName() {
        return UUID.randomUUID().toString().trim().replaceAll("-", "");
    }

    /**
     * 获取文件扩展名
     *
     * @param file
     * @return
     */
    public static String getExt(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
    public static String getExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    /**
     * 保存成功返回md5值，失败返回null
     *
     * @param urlStr
     * @param fileName
     * @param savePath
     * @return
     * @throws IOException
     */
    public static String downLoadByUrl(String urlStr, String fileName, String savePath) {
        FileOutputStream fos = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //设置超时间为3秒
            conn.setConnectTimeout(5 * 1000);
            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //得到输入流
            inputStream = conn.getInputStream();
            //获取自己数组
            byte[] getData = readInputStream(inputStream);

            String md5 = DigestUtils.md5Hex(getData);

            //文件保存位置
            File saveDir = new File(savePath);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            File file = new File(saveDir + File.separator + fileName);

            fos = new FileOutputStream(file);
            fos.write(getData);
            log.info("info:" + url + " download success");
            return md5;
        } catch (IOException e) {
            log.error("video download error , originUrl : {}", urlStr, e);
            return "";
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                log.error("close error", e);
            }
        }
    }

    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */

    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }
}
