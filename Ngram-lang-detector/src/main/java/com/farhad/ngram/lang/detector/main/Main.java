/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.main;

import com.farhad.ngram.lang.detector.identifier.Similarity;
import com.farhad.ngram.lang.detector.profile.LanguageProfile;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fnoorala
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       
         Main m= new Main();
     //make the profile for each language based on the corpus in resources
    
     //   m.profiler();
        
     // identify the language for the document   
        String document;
        if(args.length > 0) {
            
            document=args[0];
            
        }else{
            
           File file = new File("src/test/test_sp.txt");
           document = file.getAbsolutePath();
           
        }
        
     m.identifier(document);
    }
    
    public  void identifier(String documentFile){
        
         
        Similarity sim=new Similarity();
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("langid.collection").getFile());
        
        String resources=file.getParentFile().getAbsolutePath();
         
        sim.loadProfiles(resources);
        sim.similarity(documentFile);
        System.out.println("The most likely language for this document is : "+ sim.getLanguage());
        
    }
    
    
     public  void profiler(){
        LanguageProfile profile=new LanguageProfile(5);
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource("langid.collection").getFile());
        profile.construct(file.getAbsolutePath());
     }
    
}
