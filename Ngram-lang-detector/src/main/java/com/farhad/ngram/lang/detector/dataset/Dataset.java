/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.farhad.ngram.lang.detector.dataset;

import com.farhad.ngram.lang.detector.ngram.NgramExtractor;
import com.farhad.ngram.lang.detector.util.FileTools;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections4.map.MultiValueMap;
import weka.core.*;

/**
 *
 * @author farhadzn
 */
public class Dataset {

    FileTools filetools = new FileTools();

    public Instances construct(String path) {
        MultiValueMap corpus = filetools.readFile(path);
        int numClass = corpus.keySet().size();
        FastVector fvNominalVal = new FastVector(numClass);
        Iterator<String> it = corpus.keySet().iterator();

        // Create first attribute, the class
        while (it.hasNext()) {
            String theKey = (String) it.next();
            fvNominalVal.addElement(theKey);
        }

        Attribute attribute1 = new Attribute("language_class", fvNominalVal);

        // Create second attribute, the text
        FastVector fvWekaAttributes = new FastVector(2);

        Attribute attribute2 = new Attribute("text", (FastVector) null);
        fvWekaAttributes.addElement(attribute1);
        fvWekaAttributes.addElement(attribute2);

        // Build instance set with just one instance
        Instances instances = new Instances("language_detection", fvWekaAttributes, 1);
        // Set class index
        instances.setClassIndex(0);
        Instance instance = new Instance(2);

        it = corpus.keySet().iterator();
        while (it.hasNext()) {

            String theKey = (String) it.next();
            instance.setDataset(instances);
            instance.setValue(attribute1, theKey);
            
            List<String> texts = (List<String>) corpus.get(theKey);
            
            
           for (String text : texts) {
//                StringBuilder txt = new StringBuilder();
//                List<String> unigrams = NgramExtractor.gramLength(1).textPadding('_').extractGrams(text);
//                
//                for (String ngram : unigrams) {
//                    txt.append(ngram);
//                    txt.append(" ");  
//                }  
//                
//                List<String> bigrams = NgramExtractor.gramLength(2).textPadding('_').extractGrams(text);
//                
//                for (String ngram : bigrams) {
//                    txt.append(ngram);
//                    txt.append(" ");  
//                }  
//                List<String> trigrams = NgramExtractor.gramLength(3).textPadding('_').extractGrams(text);
//                for (String ngram : trigrams) {
//                    txt.append(ngram);
//                    txt.append(" ");  
//                }
//                List<String> fourgrams = NgramExtractor.gramLength(4).textPadding('_').extractGrams(text);
//
//                for (String ngram : fourgrams) {
//                    txt.append(ngram);
//                    txt.append(" ");  
//                }
//                List<String> fivegrams = NgramExtractor.gramLength(5).textPadding('_').extractGrams(text);
//               for (String ngram : fivegrams) {
//                    txt.append(ngram);
//                    txt.append(" ");  
//                }
               // txt.deleteCharAt(txt.length()-1);
                
             
                
                instance.setValue(attribute2, text);
               // System.out.println(instance);
                instances.add(instance);
            }
        }
        System.out.println("===== Instance created with reference dataset =====");
        //System.out.println(instances);
    return instances;   

    }

}
