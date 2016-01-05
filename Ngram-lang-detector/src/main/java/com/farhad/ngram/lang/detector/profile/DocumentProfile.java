/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.profile;

import com.farhad.ngram.lang.detector.ngram.NgramExtractor;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author fnoorala
 */
public class DocumentProfile {

    public DocumentProfile(int n) {
        this.ngram=n;
    }

   String text;
   int ngram;
   public  Map<String, Double> vector;

    public void load(String fileName) {
        try {
             BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF8"));
          
            String line;
            text = "";
            while ((line = reader.readLine()) != null) {
                text = text + " " + line;
            }

            reader.close();
           
            System.out.println("The document text is : "+text);
            
            text = text.toLowerCase();
            //replace number with blankspace
            text = text.replaceAll("\\p{Punct}+", "");
            text = text.replaceAll("\\d+", "");
            text = text.replaceAll("\\s+", " ");
            
        } catch (IOException e) {
            System.out.println("Problem found when reading: " + fileName);
        }
    }

    public  void toVector() {
        vector = new HashMap<>();
        
         Map<String, Integer>  grams=new HashMap<>();
                for(int n=1;n<=ngram;n++){
                    grams.putAll( NgramExtractor.gramLength(n).extractCountedGrams(text));
                     
                }
       
       

        Iterator<String> it = grams.keySet().iterator();

        while (it.hasNext()) {
            int N = grams.keySet().size();
            String key=it.next();
            double f = (double) grams.get(key) / N;
            vector.put(key, f);
        }
       

    }

}
