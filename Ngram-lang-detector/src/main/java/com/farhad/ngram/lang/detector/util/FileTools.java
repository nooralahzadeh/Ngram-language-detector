/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FileTools, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author farhadzn
 */
public class FileTools {
    
    public MultiValueMap readFile(String path){
        //Map<String, String> corpus=new HashMap<>();
        MultiValueMap corpus=new  MultiValueMap();
       try (BufferedReader br = new BufferedReader(new FileReader(path)))
		{

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				//System.out.println(sCurrentLine);
                                String[] parts = sCurrentLine.split("\t");
                                
                                String key = parts[0]; 
                                String part2 = parts[1]; 
                                part2=part2.replaceAll("\\s+", "_").toLowerCase();
                                corpus.put(key, part2);
//                                if(corpus.containsKey(key)){
//                                    String sentences=corpus.get(key);
//                                    sentences= sentences+" "+part2;
//                                    corpus.put(key, sentences);
//                                }
//                                else{
//                                     corpus.put(key, part2);
//                                }
                               
                                
			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
       return corpus;
    }
}
    

