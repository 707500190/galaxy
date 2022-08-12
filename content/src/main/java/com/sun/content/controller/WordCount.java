package com.sun.content.controller;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;


/**
 * 英文分词 词频统计
 * @author D1M
 * 
 */
public class WordCount {
 
    public static void main(String[] args) throws Exception {
       
       try {
		PrintStream ps = new PrintStream(new FileOutputStream("D:/D1M/2017113782.txt"));

		   System.setOut(ps);

		   BufferedReader br = new BufferedReader(new FileReader("D:/D1M/word.txt"));

		   List <String> list = new ArrayList<String>();

		   String readLine = null;

		   while((readLine=br.readLine())!=null){

		       String [] onlyWord = readLine.split("[^a-zA-Z]");//只有字母

		       for(String word : onlyWord){

		          if(word.length()!=0){
		              list.add(word);
		          }

		       }

		   }

		   br.close();//关闭流操作

		   Map<String,Integer> map = new TreeMap<String, Integer>();//利用 TreeMap进行统计并且排序

		   for(String mapWord : list){

		       if(map.get(mapWord)!=null){

		          map.put(mapWord, map.get(mapWord)+1);

		       }else{

		          map.put(mapWord, 1);

		       }

		   }

		   SortMap(map);
	} catch (Exception e) {
		e.printStackTrace();
	}

    }

    public static void SortMap(Map<String,Integer>oldmap){

       try {
		ArrayList<Map.Entry<String, Integer>>newList = new ArrayList<Map.Entry<String,Integer>>(oldmap.entrySet());

		   Collections.sort(newList,new Comparator<Map.Entry<String, Integer>>() {

		       @Override
		       public int compare(Entry<String, Integer> o1,

		              Entry<String, Integer> o2) {

		          return o2.getValue()-o1.getValue();//降

		       }
		   });

		   for(int i=0;i<newList.size();i++){

		       System.out.println(newList.get(i).getKey()+": "+ newList.get(i).getValue());

		   }
		   
	} catch (Exception e) {
		e.printStackTrace();
	}

    }

 

}