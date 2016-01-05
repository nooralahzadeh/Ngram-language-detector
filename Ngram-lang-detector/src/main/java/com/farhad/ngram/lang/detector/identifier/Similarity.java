/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.identifier;

import com.farhad.ngram.lang.detector.profile.DocumentProfile;
import com.farhad.ngram.lang.detector.profile.LanguageProfile;
import com.farhad.ngram.lang.detector.util.FileTools;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author farhadzn
 */
public class Similarity {
    
    //load language profiles
    LanguageProfile languageProfile;
    Map<String,Map> profiles=new HashMap<>();
    List<File> languages;
    FileTools tools=new FileTools();
    DocumentProfile docProfile = new DocumentProfile(5);
    
   public Map<String,Double> similarities=new HashMap<>();
    
           
    public void loadProfiles(String path){
        languages=tools.listFiles(path);
        languageProfile=new LanguageProfile();
        for(File lang:languages){   
            profiles.put(lang.getName().replace(".profile",""), languageProfile.loadProfile(lang.getPath()));
            
        }    
    }
    
    public void  similarity(String fileName){
       
        docProfile.load(fileName);
        docProfile.toVector();
        
        Map<String,Double> doc=docProfile.vector;
      
        Set<String> doc_ngrams=doc.keySet();
        Iterator<String> profiles_it = profiles.keySet().iterator();
        while(profiles_it.hasNext()){
            String lang=profiles_it.next();
            Map<String,Double> lang_profile= profiles.get(lang);
           
            Set<String> profile_ngrams= lang_profile.keySet();
            Set<String> intersections= Sets.intersection(doc_ngrams, profile_ngrams); 
           
            int length=intersections.size();
            
            double[] doc_vector = new double[length];
            double[] profile_vector=new double[length];
            
            for(int i=0;i< length;i++){
                
                String  ngram=(String) intersections.toArray()[i]; 
                profile_vector[i]=lang_profile.get(ngram);
                doc_vector[i]=doc.get(ngram);
             }
            
             double sim=cosineSimilarity(doc_vector,profile_vector) ; 
              similarities.put(lang, sim);
            }

            
        }
        
   public String getLanguage(){
       
       Map.Entry<String, Double> maxEntry = null;

        for (Map.Entry<String, Double> entry : similarities.entrySet())
        {
            if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0)
            {
                maxEntry = entry;
            }
        }
        return maxEntry.getKey();
       
   }     
   public double cosineSimilarity(double[] docVector1, double[] docVector2) {
        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;
        double cosineSimilarity = 0.0;

        for (int i = 0; i < docVector1.length; i++) //docVector1 and docVector2 must be of same length
        
        {
            dotProduct += docVector1[i] * docVector2[i];  //a.b
            magnitude1 += Math.pow(docVector1[i], 2);  //(a^2)
            magnitude2 += Math.pow(docVector2[i], 2); //(b^2)
        }

        magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
        magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

        if (magnitude1 != 0.0 | magnitude2 != 0.0) {
            
            cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
        } else {
            return 0.0;
        }
        return cosineSimilarity;
    }    
        
        
  
    
}
