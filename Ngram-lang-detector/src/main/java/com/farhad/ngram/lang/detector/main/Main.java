/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.main;


import com.farhad.ngram.lang.detector.classifier.LanguageIdentifier;
import com.farhad.ngram.lang.detector.classifier.Learner;
import com.farhad.ngram.lang.detector.ngram.NgramExtractor;
import com.farhad.ngram.lang.detector.util.FileTools;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author farhadzn
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
     //learningPhase();
        
        String file="/Users/farhadzn/Desktop/Hannover/test_sp.txt";
        String mdl="/Users/farhadzn/Desktop/Hannover/NB.mdl.model";
        LanguageIdentifier identify=new LanguageIdentifier();
        identify.predictLanguageClass(file, mdl);
     
        
       
        
      
    }
    
      
    public static void learningPhase(){
        
        Learner learner=new Learner("/Users/farhadzn/Desktop/Hannover/");
       // Learner learner=new Learner();
        try {
            //learner.evaluate();
          learner.learn();
           learner.saveModel("/Users/farhadzn/Desktop/Hannover/NB.mdl.model");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
