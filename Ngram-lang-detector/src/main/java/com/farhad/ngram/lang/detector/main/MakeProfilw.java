/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.main;

import com.farhad.ngram.lang.detector.dataset.LanguageProfile;
import java.util.Map;

/**
 *
 * @author fnoorala
 */
public class MakeProfilw {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
         LanguageProfile pf=new LanguageProfile();
         //pf.construct("/user/fnoorala/home/NetBeansProjects/Ngram-language-detector/Ngram-lang-detector/src/resource/langid.collection");
        Map<String,Double> en= pf.loadProfile("EN");
        System.out.println(en);
        
    }
    
}
