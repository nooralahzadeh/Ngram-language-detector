/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template FileTools, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections4.map.MultiValueMap;

/**
 *
 * @author farhadzn
 */
public class FileTools {
    
    public MultiValueMap readFile(String path) {
        //Map<String, String> corpus=new HashMap<>();
        MultiValueMap corpus = new MultiValueMap();
        
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
            
            String sCurrentLine;
            
            while ((sCurrentLine = br.readLine()) != null) {
                //System.out.println(sCurrentLine);
                String[] parts = sCurrentLine.split(",'");
                
                String key = parts[0];
                String text = parts[1];
                text = text.substring(0, text.length() - 1);
                // replace spaces with _
                // text=text.replaceAll("\\s+", "_").toLowerCase();
                //replace number and punctuations with blankspace
                text = text.replaceAll("\\p{Punct}+", "");
                text = text.replaceAll("\\d+", "");
                // System.err.println(key + "| "+ text);

                corpus.put(key, text.toLowerCase());
//                                

            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return corpus;
    }
    
    public List<File> listFiles(String path) {
        List<File> profiles = new ArrayList<>();
        File[] files = new File(path).listFiles();
        
        for (File file : files) {
            if (file.isFile()) {
               
                if (file.getName().endsWith(".profile")) {
                    profiles.add(file);
                }
            }
        }
        return profiles;
    }
}
