/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.main;

import com.farhad.ngram.lang.detector.classifier.Similarity;
import com.farhad.ngram.lang.detector.dataset.LanguageProfile;
import java.util.Map;

/**
 *
 * @author fnoorala
 */
public class MakeProfile {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // LanguageProfile profile=new LanguageProfile(3);
        // profile.construct("/Users/farhadzn/NetBeansProjects/Ngram-language-detector/Ngram-language-detector/Ngram-lang-detector/src/resource/langid.collection");
      identifier();
    }
    
    public static void identifier(){
        String test="/Users/farhadzn/NetBeansProjects/Ngram-language-detector/Ngram-language-detector/Ngram-lang-detector/test";
        Similarity sim=new Similarity();
        sim.loadProfiles("/Users/farhadzn/NetBeansProjects/Ngram-language-detector/Ngram-language-detector/Ngram-lang-detector");
        sim.similarity(test);
        System.out.println(sim.similarities);
    }
    
    
}
