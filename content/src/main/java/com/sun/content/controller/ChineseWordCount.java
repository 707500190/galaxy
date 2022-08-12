package com.sun.content.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.ansj.splitWord.analysis.ToAnalysis;

import lombok.extern.slf4j.Slf4j;

/**
 * 中文文辞，词频统计
 * 出处参考：https://blog.csdn.net/weixin_33713707/article/details/94752782
 * https://www.cnblogs.com/jiangcong/p/14820403.html
 * @author yf.zhao
 * 
 */
@Slf4j
public class ChineseWordCount {

	
	public static void wordFrequency(String userDefined) throws IOException {
        Map<String, Integer> map = new HashMap<>();

        String article = getString();
        String result = ToAnalysis.parse(article).toStringWithOutNature();
        result  = result  +  userDefined;
        
        log.info("+++++++++++++++++  result :  {} " , result);
        String[] words = result.split(",");

        for(String word: words){
            String str = word.trim();
            // 过滤空白字符
            if (str.equals(""))
                continue;
            // 过滤一些高频率的符号
            else if(str.matches("[）|（|.|，|。|+|-|“|”|：|？|\\s]"))
                continue;
            // 此处过滤长度为1的str
            else if (str.length() < 2)
                continue;

            if (!map.containsKey(word)){
                map.put(word, 1);
            } else {
                int n = map.get(word);
                map.put(word, ++n);
            }
            
            log.info("+++++++++++++++++  words str :  {} " , str);
        }

        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        List<Map.Entry<String, Integer>> list = new ArrayList<>();
        Map.Entry<String, Integer> entry;

        
        while ((entry = getMax(map)) != null){
            list.add(entry);
        }

        System.out.println(Arrays.toString(list.toArray()));
    }


    /**
     * 找出map中value最大的entry, 返回此entry, 并在map删除此entry
     * @param map
     * @return
     */
    public static Map.Entry<String, Integer> getMax(Map<String, Integer> map){
        if (map.size() == 0){
            return null;
        }
        Map.Entry<String, Integer> maxEntry = null;
        boolean flag = false;
        Iterator<Map.Entry<String, Integer>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, Integer> entry = iterator.next();
            if (!flag){
                maxEntry = entry;
                flag = true;
            }
            if (entry.getValue() > maxEntry.getValue()){
                maxEntry = entry;
            }
        }
        map.remove(maxEntry.getKey());
        return maxEntry;
    }

    /**
     * 从文件中读取待分割的文章素材.
　　　*  文件内容来自简书热门文章: https://www.jianshu.com/p/5b37403f6ba6
     * @return
     * @throws IOException
     */
    public static String getString() throws IOException {
        FileInputStream inputStream = new FileInputStream(new File("E:\\bigdata\\article-txt.txt"));
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder strBuilder = new StringBuilder();

        String line;
        while((line = reader.readLine()) != null){
            strBuilder.append(line);
        }
        reader.close();
        inputStream.close();
        return strBuilder.toString();
    }
    
    
    public static void main(String[] args) throws Exception{
    	String userDefined="我们,中国人民,中国共产党,赵英飞";
    	getString();
    	wordFrequency(userDefined);
    	
    }
    
    
}
