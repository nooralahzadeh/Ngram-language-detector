/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.main;

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
        
        FileTools filetools=new  FileTools();
        //System.out.println(filetools.readFile("/Users/farhadzn/Downloads/europarl.test.txt").get("en"));
        
       
        
        Learner learner=new Learner("/Users/farhadzn/Desktop/Hannover/europarl.test.txt");
        
        try {
            learner.evaluate();
            learner.saveARFF("/Users/farhadzn/Desktop/Hannover/training.arff");
            learner.learn();
            
            learner.saveModel("/Users/farhadzn/Desktop/Hannover/SML.mdl.txt");
        } catch (Exception ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
